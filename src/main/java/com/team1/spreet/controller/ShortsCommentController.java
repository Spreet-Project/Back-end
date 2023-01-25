package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsCommentDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.ShortsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "shortsComment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsCommentController {

	private final ShortsCommentService shortsCommentService;

	// shortsComment 등록
	@ApiOperation(value = "쇼츠 댓글 등록 API")
	@PostMapping("/{shortsId}/comment")
	public CustomResponseBody<SuccessStatusCode> saveShortsComment(@PathVariable @ApiParam(value = "댓글 등록할 쇼츠 ID") Long shortsId,
		@RequestBody @Valid @ApiParam(value = "등록할 댓글 내용") ShortsCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		shortsCommentService.saveShortsComment(shortsId, requestDto, userDetails.getUser());
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_COMMENT);
	}

	// shortsComment 수정
	@ApiOperation(value = "쇼츠 댓글 수정 API")
	@PutMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<SuccessStatusCode> updateShortsComment(@PathVariable @ApiParam(value = "댓글 수정할 쇼츠 ID") Long shortsCommentId,
		@RequestBody @Valid @ApiParam(value = "수정할 댓글 정보") ShortsCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		shortsCommentService.updateShortsComment(shortsCommentId, requestDto, userDetails.getUser());
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_COMMENT);
	}

	// shortsComment 삭제
	@ApiOperation(value = "쇼츠 댓글 삭제 API")
	@DeleteMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<SuccessStatusCode> deleteShortsComment(@PathVariable @ApiParam(value = "댓글 삭제할 쇼츠 ID") Long shortsCommentId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		shortsCommentService.deleteShortsComment(shortsCommentId, userDetails.getUser());
		return new CustomResponseBody<>(SuccessStatusCode.DELETE_COMMENT);
	}

	// shortsComment 조회
	@ApiOperation(value = "쇼츠 댓글 리스트 조회 API")
	@GetMapping("/{shortsId}/comment")
	public CustomResponseBody<List<ShortsCommentDto.ResponseDto>> getShortsCommentList(@PathVariable @ApiParam(value = "댓글 리스트를 조회할 쇼츠 ID") Long shortsId) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_COMMENTS, shortsCommentService.getCommentList(shortsId));
	}
}
