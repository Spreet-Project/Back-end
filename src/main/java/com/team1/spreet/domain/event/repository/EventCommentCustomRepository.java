package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.dto.EventCommentDto;
import java.util.List;

public interface EventCommentCustomRepository {

	// CommentList 조회
	List<EventCommentDto.ResponseDto> findAllByEventId(Long eventId);

	// Shorts 삭제에 따른 comment 의 상태(deleted) 변경
	void updateDeletedTrueByEventId(Long eventId);

	// 회원탈퇴 시 event comment soft delete
	void updateDeletedTrueByUserId(Long userId);
}
