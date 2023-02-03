package com.team1.spreet.domain.event.service;

import com.team1.spreet.domain.event.dto.EventCommentDto;
import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.event.model.EventComment;
import com.team1.spreet.domain.event.repository.EventCommentRepository;
import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventCommentService {

	private final EventRepository eventRepository;
	private final EventCommentRepository eventCommentRepository;

	public void saveEventComment(Long eventId, EventCommentDto.RequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		Event event = checkEvent(eventId);
		eventCommentRepository.saveAndFlush(
			requestDto.toEntity(requestDto.getContent(), event, user));
	}

	public void updateEventComment(Long commentId, EventCommentDto.RequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		EventComment eventComment = checkEventComment(commentId);

		if (!user.getId().equals(eventComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		eventComment.updateEventComment(requestDto.getContent());
		eventCommentRepository.saveAndFlush(eventComment);
	}

	public void deleteEventComment(Long commentId) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
		}

		EventComment eventComment = checkEventComment(commentId);

		if (!user.getUserRole().equals(UserRole.ROLE_ADMIN) && !user.getId().equals(eventComment.getUser().getId())) {
			throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
		}
		eventComment.isDeleted();
		eventCommentRepository.saveAndFlush(eventComment);
	}

	@Transactional(readOnly = true)
	public List<EventCommentDto.ResponseDto> getEventCommentList(Long eventId) {
		checkEvent(eventId);

		return eventCommentRepository.findAllByEventId(eventId);
	}

	private EventComment checkEventComment(Long commentId) {
		return eventCommentRepository.findByIdAndDeletedFalseWithUser(commentId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
		);
	}

	private Event checkEvent(Long eventId) {
		return eventRepository.findByIdAndDeletedFalse(eventId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_EVENT)
		);
	}

}