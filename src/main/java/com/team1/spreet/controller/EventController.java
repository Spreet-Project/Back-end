package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.EventDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
		EventDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(eventService.saveEvent(requestDto, Long.parseLong(userDetails.getUsername())));
	}

	// Event 게시글 수정
	@ApiOperation(value = "행사 게시글 수정 API")
	@PutMapping("/{eventId}")
	public CustomResponseBody<SuccessStatusCode> updateEvent(@ModelAttribute @Valid @ApiParam(value = "행사 게시글 수정 정보")
		EventDto.UpdateRequestDto requestDto, @PathVariable @ApiParam(value = "수정할 행사 게시글 ID")Long eventId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(eventService.updateEvent(requestDto, eventId, Long.parseLong(userDetails.getUsername())));
	}

	// Event 게시글 삭제
	@ApiOperation(value = "행사 게시글 삭제 API")
	@DeleteMapping("/{eventId}")
	public CustomResponseBody<SuccessStatusCode> deleteEvent(@PathVariable @ApiParam(value = "삭제할 행사 게시글 ID") Long eventId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(eventService.deleteEvent(eventId, Long.parseLong(userDetails.getUsername())));
	}

	// Event 게시글 상세조회
	@ApiOperation(value = "행사 게시글 상세조회 API")
	@GetMapping("/{eventId}")
	public CustomResponseBody<EventDto.ResponseDto> getEvent(@PathVariable @ApiParam(value = "조회할 행사 게시글 ID") Long eventId) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_EVENT, eventService.getEvent(eventId));
	}

	// Event 전체 조회
	@ApiOperation(value = "행사 게시글 상세조회 API")
	@GetMapping
	public CustomResponseBody<List<EventDto.ResponseDto>> getEventList() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_EVENT_LIST, eventService.getEventList());
	}
}
