package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedCommentDto;
import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.FeedComment;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.FeedCommentRepository;
import com.team1.spreet.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class FeedCommentService {

    private final FeedRepository feedRepository;
    private final FeedCommentRepository feedCommentRepository;
    @Transactional
    public CustomResponseBody<FeedCommentDto.ResponseDto> saveFeedComment(Long feedId, FeedCommentDto.RequestDto requestDto, User user) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
        FeedComment feedComment = new FeedComment(requestDto.getContent(), feed, user);
        feedCommentRepository.save(feedComment);
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_FEED_COMMENT, new FeedCommentDto.ResponseDto(feedComment, user.getNickname()));
    }
    @Transactional
    public CustomResponseBody<FeedCommentDto.ResponseDto> updateFeedComment(Long commentId, FeedCommentDto.RequestDto requestDto, User user){
        FeedComment feedComment = CheckFeedComment(commentId);    //commentId로 comment 찾기
        feedComment.update(requestDto.getContent());
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_FEED_COMMENT, new FeedCommentDto.ResponseDto(feedComment, user.getNickname()));
    }

    @Transactional
    public CustomResponseBody<SuccessStatusCode> deleteFeedComment(Long commentId, User user) {
        FeedComment feedComment = CheckFeedComment(commentId);    //commentId로 comment 찾기
        feedComment.setDeleted();
        return new CustomResponseBody<>(SuccessStatusCode.DELETE_FEED_COMMENT);
    }
    //commentId로 comment 찾기
    private FeedComment CheckFeedComment(Long commentId){
        return feedCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED_COMMENT)
        );
    }
}
