package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.dto.EventDto;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;

public interface EventCustomRepository {

	// event 게시글 전체조회
	List<EventDto.ResponseDto> findAllSortByNew();

	// event 게시글 상세조회
	EventDto.ResponseDto findByEventId(Long eventId);

	// 회원이 작성한 게시글 조회
	List<MyPageDto.PostResponseDto> findByUserId(String classification, Long page, Long userId);

	// 회원탈퇴 시 event soft delete
	void updateDeletedTrueByUserId(Long userId);
}
