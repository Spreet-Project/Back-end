package com.team1.spreet.domain.event.service;

import com.team1.spreet.domain.event.dto.EventCommentDto;
import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.event.model.EventComment;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.domain.event.repository.EventCommentRepository;
import com.team1.spreet.domain.event.repository.EventRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventCommentService {

    private final EventRepository eventRepository;
    private final EventCommentRepository eventCommentRepository;

    public void saveEventComment(Long eventId, EventCommentDto.RequestDto requestDto, User user) {
        Event event = checkEvent(eventId);
        eventCommentRepository.saveAndFlush(requestDto.toEntity(requestDto.getContent(), event, user));
    }

    public void updateEventComment(Long commentId, EventCommentDto.RequestDto requestDto, User user){
        EventComment eventComment = checkEventComment(commentId);
        if(checkOwner(user, eventComment)){
            eventCommentRepository.updateContentByIdAndIsDeletedFalse(requestDto.getContent(), commentId);
        }
    }

    public void deleteEventComment(Long commentId, User user) {
        EventComment eventComment = checkEventComment(commentId);
        if (checkOwner(user, eventComment)) {
            eventCommentRepository.updateIsDeletedById(commentId);
        }
    }

    @Transactional(readOnly = true)
    public List<EventCommentDto.ResponseDto> getEventCommentList(Long eventId) {
        checkEvent(eventId);
        List<EventComment> eventComments = eventCommentRepository.findByEventIdWithUserAndIsDeletedFalseOrderByCreatedAtDesc(eventId);
        List<EventCommentDto.ResponseDto> eventCommentList = new ArrayList<>();
        for (EventComment eventComment : eventComments) {
            eventCommentList.add(new EventCommentDto.ResponseDto(eventComment));
        }
        return eventCommentList;
    }

    private boolean checkOwner(User user, EventComment eventComment) {
        if(!(user.getUserRole()== UserRole.ROLE_ADMIN || user.getId().equals(eventComment.getUser().getId()))){
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        }
        return true;
    }

    private EventComment checkEventComment(Long commentId) {
        return eventCommentRepository.findByIdAndIsDeletedFalse(commentId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
        );
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findByIdAndIsDeletedFalse(eventId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_EVENT)
        );
    }

}