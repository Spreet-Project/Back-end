package com.team1.spreet.service;

import com.team1.spreet.dto.FeedCommentDto;
import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.FeedComment;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.FeedCommentRepository;
import com.team1.spreet.repository.FeedRepository;
import com.team1.spreet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCommentService {

    private final FeedRepository feedRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final UserRepository userRepository;
    @Transactional
    public FeedCommentDto.ResponseDto saveFeedComment(Long feedId, FeedCommentDto.RequestDto requestDto, UserDetails userDetails) {
        User user = checkUser(userDetails);    //userDetails로 user 찾기
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
        FeedComment feedComment = new FeedComment(requestDto.getContent(), feed, user);
        feedCommentRepository.save(feedComment);
        return new FeedCommentDto.ResponseDto(feedComment);
    }
    @Transactional
    public FeedCommentDto.ResponseDto updateFeedComment(Long commentId, FeedCommentDto.RequestDto requestDto, UserDetails userDetails){
        User user = checkUser(userDetails);    //userDetails로 user 찾기
        FeedComment feedComment = CheckFeedComment(user.getId(), commentId);    //userId, commentId로 comment 찾기
        feedComment.update(requestDto.getContent());
        return new FeedCommentDto.ResponseDto(feedComment);
    }

    @Transactional
    public SuccessStatusCode deleteFeedComment(Long commentId, UserDetails userDetails) {
        User user = checkUser(userDetails);    //userDetails로 user 찾기
        FeedComment feedComment = CheckFeedComment(user.getId(), commentId);    //userId, commentId로 comment 찾기
        feedComment.setDeleted();
        return SuccessStatusCode.DELETE_FEED_COMMENT;
    }
    //commentId로 comment 찾기
    private FeedComment CheckFeedComment(Long userId, Long commentId){
        return feedCommentRepository.findByUserIdAndId(userId, commentId);
    }
    //user 정보 가져오기
    private User checkUser(UserDetails userDetails) {
        return userRepository.findById(Long.parseLong(userDetails.getUsername())).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
    }
    //댓글 조회
    public List<FeedCommentDto.ResponseDto> getFeedComment(Long feedId) {
        List<FeedCommentDto.ResponseDto> commentList = new ArrayList<>();
        List<FeedComment> feedCommentList= feedCommentRepository.findByFeedIdAndIsDeletedFalseOrderByCreatedAtDesc(feedId);
        for (FeedComment feedComment : feedCommentList) {
            commentList.add(new FeedCommentDto.ResponseDto(feedComment));
        }
        return commentList;
    }
}
