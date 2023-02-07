package com.team1.spreet.domain.feed.service;

import com.team1.spreet.domain.admin.service.BadWordService;
import com.team1.spreet.domain.alarm.service.AlarmService;
import com.team1.spreet.domain.feed.dto.FeedCommentDto;
import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.model.FeedComment;
import com.team1.spreet.domain.feed.repository.FeedCommentRepository;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCommentService {

    private final FeedRepository feedRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final BadWordService badWordService;
    private final AlarmService alarmService;

    @Transactional
    public void saveFeedComment(Long feedId, FeedCommentDto.RequestDto requestDto) {
        User user = SecurityUtil.getCurrentUser();
        if(user == null){
            throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
        }
        Feed feed = checkFeed(feedId);

        String content = badWordService.checkBadWord(requestDto.getContent());
        feedCommentRepository.saveAndFlush(requestDto.toEntity(content, feed, user));

        if (!feed.getUser().getId().equals(user.getId())) {
            alarmService.send(user.getId(),
                    "💬" + feed.getUser().getNickname() + "님! " + "작성하신 feed에 댓글 알림이 도착했어Yo!\n",
                    "https://www.spreet.co.kr/api/feed/" + feed.getId(),
                    feed.getUser().getId());
        }
    }

    @Transactional
    public void updateFeedComment(Long commentId, FeedCommentDto.RequestDto requestDto){
        User user = SecurityUtil.getCurrentUser();
        if(user == null){
            throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
        }
        FeedComment feedComment = checkFeedComment(commentId);
        if(checkOwner(feedComment, user.getId())){
            String content = badWordService.checkBadWord(requestDto.getContent());
            feedComment.update(content);
        }
    }

    @Transactional
    public void deleteFeedComment(Long commentId) {
        User user = SecurityUtil.getCurrentUser();
        if(user == null){
            throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
        }
        FeedComment feedComment = checkFeedComment(commentId);    //userId, commentId로 comment 찾기
        if(checkOwner(feedComment, user.getId())) {
            feedComment.delete();
        }
    }
    //댓글 조회
    @Transactional(readOnly = true)
    public List<FeedCommentDto.ResponseDto> getFeedComment(Long feedId) {
        return feedCommentRepository.findAllByFeedId(feedId);
    }

    private Feed checkFeed(Long feedId) {
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
    private FeedComment checkFeedComment(Long commentId){
        return feedCommentRepository.findById(commentId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_COMMENT)
        );
    }
}
