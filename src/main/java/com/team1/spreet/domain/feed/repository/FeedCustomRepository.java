package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.feed.dto.FeedDto;

import java.util.List;

public interface FeedCustomRepository {
    //feed 상세조회
    FeedDto.ResponseDto findAllByIdAndUserId(Long feedId, Long userId);

	// 회원이 작성한 feed 조회
	List<MyPageDto.PostResponseDto> findByUserId(String classification, Long page, Long userId);
    List<FeedDto.ResponseDto> findAllOrderByCreatedAtDesc(Long page, Long size, Long userId);

    List<FeedDto.SimpleResponseDto> getSimpleFeed();
}
