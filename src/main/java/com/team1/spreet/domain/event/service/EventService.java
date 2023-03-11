package com.team1.spreet.domain.event.service;

import com.team1.spreet.domain.admin.service.BadWordService;
import com.team1.spreet.domain.alarm.service.AlarmService;
import com.team1.spreet.domain.event.dto.EventDto;
import com.team1.spreet.domain.event.model.AreaCode;
import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.event.repository.EventCommentRepository;
import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.subscribe.model.Subscribe;
import com.team1.spreet.domain.subscribe.repository.SubscribeRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import com.team1.spreet.global.util.SecurityUtil;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

	private final AwsS3Service awsS3Service;
	private final EventRepository eventRepository;
	private final EventCommentRepository eventCommentRepository;
	private final SubscribeRepository subscribeRepository;
	private final AlarmService alarmService;
	private final BadWordService badWordService;

	// Event 게시글 등록
	@Caching(evict = {
		@CacheEvict(cacheNames = "eventList", allEntries = true),
		@CacheEvict(cacheNames = "eventByArea", allEntries = true)
	})
	public void saveEvent(EventDto.RequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		String title = badWordService.checkBadWord(requestDto.getTitle());
		String content = badWordService.checkBadWord(requestDto.getContent());

		// 어느 지역인지 주소 확인
		AreaCode areaCode = null;
		for (AreaCode code : AreaCode.values()) {
			if (requestDto.getLocation().substring(0, 2).equals(code.value())) {
				areaCode = code;
			}
		}

		String eventImageUrl;
		try {
			eventImageUrl = awsS3Service.uploadImage(requestDto.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Event event = eventRepository.saveAndFlush(requestDto.toEntity(title, content, eventImageUrl, areaCode, user));
		alarmToSubscriber(user, event);
	}

	// Event 게시글 수정
	@Caching(evict = {
		@CacheEvict(cacheNames = "eventList", allEntries = true),
		@CacheEvict(cacheNames = "eventByArea", allEntries = true)
	})
	public void updateEvent(EventDto.UpdateRequestDto requestDto, Long eventId) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		Event event = getEventWithUserIfExists(eventId);

		if (!event.getUser().getId().equals(user.getId())) {    // 수정하려는 유저가 작성자가 아닌 경우
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String title = badWordService.checkBadWord(requestDto.getTitle());
		String content = badWordService.checkBadWord(requestDto.getContent());

		// 어느 지역인지 주소 확인
		AreaCode areaCode = null;
		for (AreaCode code : AreaCode.values()) {
			if (requestDto.getLocation().substring(0, 2).equals(code.value())) {
				areaCode = code;
			}
		}

		String eventImageUrl;
		if (!requestDto.getFile().isEmpty()) {
			//첨부파일 수정시 기존 첨부파일 삭제
			String fileName = event.getEventImageUrl().split(".com/")[1];
			awsS3Service.deleteFile(fileName);

			//새로운 파일 업로드
			try {
				eventImageUrl = awsS3Service.uploadImage(requestDto.getFile());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			//첨부파일 수정 안함
			eventImageUrl = event.getEventImageUrl();
		}
		event.update(title, content, requestDto.getLocation(), requestDto.getDate(),
			requestDto.getTime(), eventImageUrl, areaCode);
	}

	// Event 게시글 삭제
	@Caching(evict = {
		@CacheEvict(cacheNames = "eventList", allEntries = true),
		@CacheEvict(cacheNames = "eventByArea", allEntries = true)
	})
	public void deleteEvent(Long eventId) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		Event event = getEventWithUserIfExists(eventId);

		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !event.getUser().getId().equals(user.getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String fileName = event.getEventImageUrl().split(".com/")[1];
		awsS3Service.deleteFile(fileName);
		deleteEventById(event);
	}

	// Event 게시글 상세조회
	@Transactional(readOnly = true)
	public EventDto.ResponseDto getEvent(Long eventId) {
		if (eventRepository.findByIdAndDeletedFalse(eventId).isEmpty()) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_EVENT);
		}
		return eventRepository.findByEventId(eventId);
	}

	// Event 게시글 전체조회
	@Cacheable(cacheNames = "eventList")
	@Transactional(readOnly = true)
	public List<EventDto.ResponseDto> getEventList() {
		return eventRepository.findAllSortByNew();
	}

	// Event 게시글 지역별 조회
	@Cacheable(key = "#areaCode", cacheNames = "eventByArea")
	@Transactional(readOnly = true)
	public List<EventDto.ResponseDto> getEventListByAreaCode(AreaCode areaCode) {
		return eventRepository.findAllByAreaCode(areaCode);
	}

	// Event 게시글 기간별 조회
	public List<EventDto.ResponseDto> getEventListByDate(String startDate, String endDate) {
		// 입력된 날짜가 빈값일 때
		if (startDate.equals("")) {
			startDate = "0001-01-01";
		}

		if (endDate.equals("")) {
			endDate = "9999-12-31";
		}
		return eventRepository.findAllByDate(startDate, endDate);
	}

	// Event 게시글이 존재하는지 확인
	private Event getEventWithUserIfExists(Long eventId) {
		return eventRepository.findByIdAndDeletedFalseWithUser(eventId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_EVENT)
		);
	}

	private void deleteEventById(Event event) {
		eventCommentRepository.updateDeletedTrueByEventId(event.getId());
		event.isDeleted();
	}

	// 구독자에게 알림 보내기
	private void alarmToSubscriber(User user, Event event) {
		List<Subscribe> subscribes = subscribeRepository.findByPublisher(user).orElse(null);
		if (subscribes != null) {
			for (Subscribe subscribe : subscribes) {
				alarmService.send(user.getId(),
					"🤸🏻" + user.getNickname() + "님의 " + "새로운 행사 정보가 등록되었어Yo!\n" + event.getTitle(),
					"https://www.spreet.co.kr/api/event/" + event.getId(),
					subscribe.getSubscriber().getId());
			}
		}
	}
}
