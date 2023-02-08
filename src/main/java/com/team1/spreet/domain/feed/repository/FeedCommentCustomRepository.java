package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.dto.FeedCommentDto;

import java.util.List;

public interface FeedCommentCustomRepository {

    List<FeedCommentDto.ResponseDto> findAllByFeedId(Long feedId);

    void updateDeletedTrueByFeedId(Long feedId);

    void updateDeletedTrueByUserId(Long userId);
}
