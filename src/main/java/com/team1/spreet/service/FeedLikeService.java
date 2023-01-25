package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedLikeDto;
import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.FeedLike;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.FeedLikeRepository;
import com.team1.spreet.repository.FeedRepository;
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
