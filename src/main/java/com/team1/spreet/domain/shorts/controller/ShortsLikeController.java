package com.team1.spreet.domain.shorts.controller;

import com.team1.spreet.domain.shorts.dto.ShortsLikeDto;
import com.team1.spreet.domain.shorts.service.ShortsLikeService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "shortsLike")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts/like")
public class ShortsLikeController {
	private final ShortsLikeService shortsLikeService;

	// shortsLike 등록
	@ApiOperation(value = "쇼츠 좋아요 등록/취소 API")
	@PostMapping("/{shortsId}")
	public CustomResponseBody<ShortsLikeDto.ResponseDto> setShortsLike(
		@PathVariable @ApiParam(value = "좋아요 등록/취소 하기 위한 쇼츠 ID") Long shortsId) {
		return shortsLikeService.setShortsLike(shortsId);
	}
}
