package com.team1.spreet.service;

import com.team1.spreet.dto.EventCommentDto;
import com.team1.spreet.entity.Event;
import com.team1.spreet.entity.EventComment;
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
public class EventCommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventCommentRepository eventCommentRepository;

    public SuccessStatusCode saveEventComment(Long eventId, EventCommentDto.RequestDto requestDto, long userId) {
        User user = getUser(userId);
        Event event = checkEvent(eventId);
        eventCommentRepository.saveAndFlush(requestDto.toEntity(requestDto.getContent(), event, user));
        return SuccessStatusCode.SAVE_COMMENT;
    }

    public SuccessStatusCode updateEventComment(Long commentId, EventCommentDto.RequestDto requestDto, Long userId){
        User user = getUser(userId);
        EventComment eventComment = checkEventComment(commentId);
        if(checkOwner(user, eventComment)){
            eventCommentRepository.updateContentByIdAndIsDeletedFalse(requestDto.getContent(), commentId);
        }
        return SuccessStatusCode.UPDATE_COMMENT;
    }
    public SuccessStatusCode deleteEventComment(Long commentId, long userId) {
        User user = getUser(userId);
        EventComment eventComment = checkEventComment(commentId);
        if (checkOwner(user, eventComment)) {
            eventCommentRepository.updateIsDeletedById(commentId);
        }
        return SuccessStatusCode.DELETE_COMMENT;
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
                ()-> new RestApiException(ErrorStatusCode.NOT_FOUND_EVENT)
        );
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
    }



}