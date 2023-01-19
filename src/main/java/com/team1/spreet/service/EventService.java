package com.team1.spreet.service;

import com.team1.spreet.dto.EventDto;
import com.team1.spreet.entity.Event;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.EventCommentRepository;
import com.team1.spreet.repository.EventRepository;
import com.team1.spreet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

	private final AwsS3Service awsS3Service;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final EventCommentRepository eventCommentRepository;

	// Event 게시글 등록
	public SuccessStatusCode saveEvent(EventDto.RequestDto requestDto, Long userId) {
		User user = getUser(userId);

		String eventImageUrl = awsS3Service.uploadFile(requestDto.getFile());

		eventRepository.saveAndFlush(requestDto.toEntity(eventImageUrl, user));

		return SuccessStatusCode.SAVE_EVENT;
	}

	// Event 게시글 수정
	public SuccessStatusCode updateEvent(EventDto.UpdateRequestDto requestDto, Long eventId, Long userId) {
		Event event = checkEvent(eventId);
		String eventImageUrl;

		if (checkOwner(event, userId)) {
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
		return SuccessStatusCode.UPDATE_EVENT;
	}

	// Event 게시글 삭제
	public SuccessStatusCode deleteEvent(Long eventId, Long userId) {
		User user = getUser(userId);

		Event event = checkEvent(eventId);

		if (user.getUserRole() == UserRole.ROLE_ADMIN ||checkOwner(event, userId)) {
			eventCommentRepository.updateIsDeletedTrueByEventId(eventId);
			eventRepository.updateIsDeletedTrue(eventId);
		}
		return SuccessStatusCode.DELETE_EVENT;
	}

	// Event 게시글 상세조회
	@Transactional(readOnly = true)
	public EventDto.ResponseDto getEvent(Long eventId) {
		Event event = checkEvent(eventId);

		return new EventDto.ResponseDto(event, event.getUser().getNickname(), event.getUser().getProfileImage());
	}

	// Event 게시글 전체조회
	@Transactional(readOnly = true)
	public List<EventDto.ResponseDto> getEventList() {
		List<Event> events = eventRepository.findAllByIsDeletedFalseWithUserOrderByCreatedAtDesc();
		List<EventDto.ResponseDto> eventList = new ArrayList<>();

		for (Event event : events) {
			eventList.add(new EventDto.ResponseDto(event, event.getUser().getNickname(),
				event.getUser().getProfileImage()));
		}
		return eventList;
	}

	// Event 게시글이 존재하는지 확인
	private Event checkEvent(Long eventId) {
		return eventRepository.findByIdAndIsDeletedFalse(eventId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_EVENT)
		);
	}

	// event 작성자와 user 가 같은지 확인
	private boolean checkOwner(Event event, Long userId) {
		if (!event.getUser().getId().equals(userId)) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		return true;
	}

	// User 객체 가져오기
	private User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER));
	}

}
