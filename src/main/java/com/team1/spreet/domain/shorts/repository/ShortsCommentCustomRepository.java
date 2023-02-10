package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.dto.ShortsCommentDto.ResponseDto;
import java.util.List;

public interface ShortsCommentCustomRepository {

	// CommentList 조회
	List<ResponseDto> findAllByShortsId(Long shortsId);

	// Shorts 삭제에 따른 comment 의 상태(deleted) 변경
	void updateDeletedTrueByShortsId(Long shortsId);
	void updateDeletedTrueByUserId(Long userId);
}
