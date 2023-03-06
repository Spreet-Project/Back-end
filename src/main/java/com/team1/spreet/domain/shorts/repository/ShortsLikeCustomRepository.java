package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;

public interface ShortsLikeCustomRepository {
	// 회원이 좋아요한 게시글 목록 조회
	List<MyPageDto.PostResponseDto> findAllByUserId(Long page, Long userId);
}
