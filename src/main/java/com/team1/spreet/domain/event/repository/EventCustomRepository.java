package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.dto.EventDto;
import java.util.List;

public interface EventCustomRepository {

	// event 게시글 전체조회
	List<EventDto.ResponseDto> findAllSortByNew();

	// event 게시글 상세조회
	EventDto.ResponseDto findByEventId(Long eventId);
}
