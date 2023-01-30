package com.team1.spreet.domain.feed.service;

import com.team1.spreet.domain.feed.dto.FeedCommentDto;
import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.model.FeedComment;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.domain.feed.repository.FeedCommentRepository;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public void saveFeedComment(Long feedId, FeedCommentDto.RequestDto requestDto, User user) {
        Feed feed = isFeed(feedId);
        FeedComment feedComment = new FeedComment(requestDto.getContent(), feed, user);
        feedCommentRepository.save(feedComment);
    }
    @Transactional
    public void updateFeedComment(Long commentId, FeedCommentDto.RequestDto requestDto, User user){
        FeedComment feedComment = isFeedComment(commentId);
        if(checkOwner(feedComment, user.getId())){
        feedComment.update(requestDto.getContent());
        }
    }

    @Transactional
    public void deleteFeedComment(Long commentId, User user) {
        isFeedComment(commentId);    //userId, commentId로 comment 찾기
        feedCommentRepository.updateIsDeletedTrueById(commentId);
    }
    //댓글 조회
    public List<FeedCommentDto.ResponseDto> getFeedComment(Long feedId) {
        List<FeedCommentDto.ResponseDto> commentList = new ArrayList<>();
        List<FeedComment> feedCommentList= feedCommentRepository.findByFeedIdAndOrderByCreatedAtDesc(feedId);
        for (FeedComment feedComment : feedCommentList) {
            commentList.add(new FeedCommentDto.ResponseDto(feedComment));
        }
        return commentList;
    }

    private Feed isFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }
    private boolean checkOwner(FeedComment feedComment, Long userId) {
        if (!userId.equals(feedComment.getUser().getId())) {
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        }
        return true;
    }
    private FeedComment isFeedComment(Long commentId){
        return feedCommentRepository.findById(commentId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
        );
    }

}
