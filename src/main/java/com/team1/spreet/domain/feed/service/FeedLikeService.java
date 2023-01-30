package com.team1.spreet.domain.feed.service;

import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.domain.feed.dto.FeedLikeDto;
import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.model.FeedLike;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.domain.feed.repository.FeedLikeRepository;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final FeedRepository feedRepository;
    //feed 좋아요
    @Transactional
    public CustomResponseBody<FeedLikeDto.ResponseDto> setFeedLike(Long feedId, User user) {
        Feed feed = isFeed(feedId);    //feedId로 feed 찾기
        FeedLike feedLike = feedLikeRepository.findByUserIdAndFeedId(user.getId(), feedId).orElse(null);
        if (feedLike!=null) {
            feedLikeRepository.delete(feedLike);
            return new CustomResponseBody<>(SuccessStatusCode.DISLIKE, new FeedLikeDto.ResponseDto(false));
        }else{
            feedLikeRepository.save(new FeedLike(user, feed));
            return new CustomResponseBody<>(SuccessStatusCode.LIKE, new FeedLikeDto.ResponseDto(true));
        }
    }
    //feed 찾기
    private Feed isFeed(Long feedId){
        return feedRepository.findById(feedId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_FEED)
        );
    }
}
