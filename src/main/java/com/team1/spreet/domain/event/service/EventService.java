package com.team1.spreet.domain.event.service;

import com.team1.spreet.domain.event.dto.EventDto;
import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.event.repository.EventCommentRepository;
import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import com.team1.spreet.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

	private final AwsS3Service awsS3Service;
	private final EventRepository eventRepository;
	private final EventCommentRepository eventCommentRepository;

	// Event 게시글 등록
	public void saveEvent(EventDto.RequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		String eventImageUrl = awsS3Service.uploadFile(requestDto.getFile());

		eventRepository.saveAndFlush(requestDto.toEntity(eventImageUrl, user));
	}

	// Event 게시글 수정
	public void updateEvent(EventDto.UpdateRequestDto requestDto, Long eventId) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		Event event = getEventWithUserIfExists(eventId);

		if (!event.getUser().getId().equals(user.getId())) {    // 수정하려는 유저가 작성자가 아닌 경우
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}

		String eventImageUrl;
		if (!requestDto.getFile().isEmpty()) {
			//첨부파일 수정시 기존 첨부파일 삭제
			String fileName = event.getEventImageUrl().split(".com/")[1];
			awsS3Service.deleteFile(fileName);

			//새로운 파일 업로드
			eventImageUrl = awsS3Service.uploadFile(requestDto.getFile());
		} else {
			//첨부파일 수정 안함
			eventImageUrl = event.getEventImageUrl();
		}
		event.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getLocation(),
			requestDto.getDate(), requestDto.getTime(), eventImageUrl);
	}

	// Event 게시글 삭제
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
	@Transactional(readOnly = true)
	public List<EventDto.ResponseDto> getEventList() {
		return eventRepository.findAllSortByNew();
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
}
