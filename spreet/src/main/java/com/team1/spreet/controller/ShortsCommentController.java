package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.ShortsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsCommentController {

	private final ShortsCommentService shortsCommentService;

	// shorts comment 생성
	@PostMapping("/{shortsId}/comment")
	public CustomResponseBody<ShortsCommentDto.ResponseDto> saveShortsComment(@PathVariable Long shortsId,
		@RequestBody ShortsCommentDto.RequestDto requestDto) {
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_SHORTS_COMMENT, shortsCommentService.saveShortsComment(shortsId, requestDto));
	}

	@PutMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<ShortsCommentDto.ResponseDto> updateShortsComment(@PathVariable Long shortsCommentId,
		@RequestBody ShortsCommentDto.RequestDto requestDto) {
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_SHORTS_COMMENT, shortsCommentService.updateShortsComment(shortsCommentId, requestDto));
	}

	@DeleteMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<ShortsCommentDto.ResponseDto> deleteShortsComment(@PathVariable Long shortsCommentId) {
		return new CustomResponseBody<>(shortsCommentService.deleteShortsComment(shortsCommentId));
	}
}
