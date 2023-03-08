package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;

public interface FeedLikeCustomRepository {
	// 회원이 좋아요한 게시글 목록 조회
	List<MyPageDto.PostResponseDto> findAllByUserId(Long page, Long userId);
}
