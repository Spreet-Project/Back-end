package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;

public interface FeedCustomRepository {

	// 회원이 작성한 feed 조회
	List<MyPageDto.PostResponseDto> findByUserId(String classification, Long page, Long userId);
}
