package com.team1.spreet.domain.shorts.controller;

import com.team1.spreet.domain.shorts.dto.ShortsCommentDto;
import com.team1.spreet.domain.shorts.service.ShortsCommentService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	public CustomResponseBody<SuccessStatusCode> saveShortsComment(
		@PathVariable @ApiParam(value = "댓글 등록할 쇼츠 ID") Long shortsId,
		@RequestBody @Valid @ApiParam(value = "등록할 댓글 내용") ShortsCommentDto.RequestDto requestDto) {
		shortsCommentService.saveShortsComment(shortsId, requestDto);
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_COMMENT);
	}

	// shortsComment 수정
	@ApiOperation(value = "쇼츠 댓글 수정 API")
	@PutMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<SuccessStatusCode> updateShortsComment(
		@PathVariable @ApiParam(value = "댓글 수정할 쇼츠 ID") Long shortsCommentId,
		@RequestBody @Valid @ApiParam(value = "수정할 댓글 정보") ShortsCommentDto.RequestDto requestDto) {
		shortsCommentService.updateShortsComment(shortsCommentId, requestDto);
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_COMMENT);
	}

	// shortsComment 삭제
	@ApiOperation(value = "쇼츠 댓글 삭제 API")
	@DeleteMapping("/comment/{shortsCommentId}")
	public CustomResponseBody<SuccessStatusCode> deleteShortsComment(
		@PathVariable @ApiParam(value = "댓글 삭제할 쇼츠 ID") Long shortsCommentId) {
		shortsCommentService.deleteShortsComment(shortsCommentId);
		return new CustomResponseBody<>(SuccessStatusCode.DELETE_COMMENT);
	}

	// shortsComment 조회
	@ApiOperation(value = "쇼츠 댓글 리스트 조회 API")
	@GetMapping("/{shortsId}/comment")
	public CustomResponseBody<List<ShortsCommentDto.ResponseDto>> getShortsCommentList(
		@PathVariable @ApiParam(value = "댓글 리스트를 조회할 쇼츠 ID") Long shortsId) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_COMMENTS, shortsCommentService.getCommentList(shortsId));
	}
}
