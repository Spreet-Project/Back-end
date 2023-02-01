package com.team1.spreet.domain.event.controller;

import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.domain.event.dto.EventDto;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.domain.event.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "event")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {

	private final EventService eventService;

	// Event 게시글 등록
	@ApiOperation(value = "행사 게시글 등록 API")
	@PostMapping
	public CustomResponseBody<SuccessStatusCode> saveEvent(@ModelAttribute @Valid @ApiParam(value = "행사 게시글 등록 정보")
		EventDto.RequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		eventService.saveEvent(requestDto, userDetails.getUser());
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_EVENT);
	}

	// Event 게시글 수정
	@ApiOperation(value = "행사 게시글 수정 API")
	@PutMapping("/{eventId}")
	public CustomResponseBody<SuccessStatusCode> updateEvent(@ModelAttribute @Valid @ApiParam(value = "행사 게시글 수정 정보")
		EventDto.UpdateRequestDto requestDto, @PathVariable @ApiParam(value = "수정할 행사 게시글 ID")Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		eventService.updateEvent(requestDto, eventId, userDetails.getUser());
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_EVENT);
	}

	// Event 게시글 삭제
	@ApiOperation(value = "행사 게시글 삭제 API")
	@DeleteMapping("/{eventId}")
	public CustomResponseBody<SuccessStatusCode> deleteEvent(@PathVariable @ApiParam(value = "삭제할 행사 게시글 ID") Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		eventService.deleteEvent(eventId, userDetails.getUser());
		return new CustomResponseBody<>(SuccessStatusCode.DELETE_EVENT);
	}

	// Event 게시글 상세조회
	@ApiOperation(value = "행사 게시글 상세조회 API")
	@GetMapping("/{eventId}")
	public CustomResponseBody<EventDto.ResponseDto> getEvent(@PathVariable @ApiParam(value = "조회할 행사 게시글 ID") Long eventId) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_EVENT, eventService.getEvent(eventId));
	}

	// Event 전체 조회
	@ApiOperation(value = "행사 게시글 전체조회 API")
	@GetMapping
	public CustomResponseBody<List<EventDto.ResponseDto>> getEventList() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_EVENT_LIST, eventService.getEventList());
	}
}
