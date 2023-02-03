package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.dto.FeedDto;

import java.util.List;

public interface FeedCustomRepository {
    FeedDto.ResponseDto findByIdAndUserId(Long feedId, Long userId);

    List<FeedDto.ResponseDto> findByOrderByCreatedAtDesc(Long page, Long size, Long userId);

    List<FeedDto.SimpleResponseDto> getSimpleFeed();
}
