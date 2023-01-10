package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.ShortsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "shortsComment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsCommentController {

	private final ShortsCommentService shortsCommentService;

	// shortsComment 등록
	@ApiOperation(value = "쇼츠 댓글 등록 API")
	@PostMapping("/{shortsId}/comment")
	public CustomResponseBody<ShortsCommentDto.ResponseDto> saveShortsComment(@PathVariable Long shortsId,
		@RequestBody @Valid ShortsCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_SHORTS_COMMENT, shortsCommentService.saveShortsComment(shortsId, requestDto, Long.parseLong(userDetails.getUsername())));
	}

	// shortsComment 수정
	@ApiOperation(value = "쇼츠 댓글 수정 API")
	@PutMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<ShortsCommentDto.ResponseDto> updateShortsComment(@PathVariable Long shortsCommentId,
		@RequestBody @Valid ShortsCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_SHORTS_COMMENT, shortsCommentService.updateShortsComment(shortsCommentId, requestDto, Long.parseLong(userDetails.getUsername())));
	}

	// shortsComment 삭제
	@ApiOperation(value = "쇼츠 댓글 삭제 API")
	@DeleteMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<ShortsCommentDto.ResponseDto> deleteShortsComment(@PathVariable Long shortsCommentId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsCommentService.deleteShortsComment(shortsCommentId, Long.parseLong(userDetails.getUsername())));
	}
}
