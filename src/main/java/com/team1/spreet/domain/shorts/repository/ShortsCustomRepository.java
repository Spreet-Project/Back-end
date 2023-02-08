package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.shorts.dto.ShortsDto;
import com.team1.spreet.domain.shorts.model.Category;
import java.util.List;

public interface ShortsCustomRepository {

	// 메인화면 카테고리별 좋아요순 shorts 10건
	List<ShortsDto.MainResponseDto> findMainShortsByCategory(Category category);

	// 최신순 + 카테고리 (shorts 화면)
	List<ShortsDto.ResponseDto> findAllSortByNewAndCategory(Category category, Long page, Long userId);

	// 좋아요순 + 카테고리 (shorts 화면)
	List<ShortsDto.ResponseDto> findAllSortByPopularAndCategory(Category category, Long page, Long userId);

	// shorts 상세조회
	ShortsDto.ResponseDto findByIdAndUserId(Long shortsId, Long userId);

	// 회원이 작성한 shorts 조회
	List<MyPageDto.PostResponseDto> findAllByUserId(String classification, Long page, Long userId);
}
