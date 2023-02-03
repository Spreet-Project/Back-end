package com.team1.spreet.domain.shorts.controller;

import com.team1.spreet.domain.shorts.dto.ShortsDto;
import com.team1.spreet.domain.shorts.model.Category;
import com.team1.spreet.domain.shorts.service.ShortsService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "shorts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsController {

	private final ShortsService shortsService;

	// shorts 등록
	@ApiOperation(value = "쇼츠 등록 API")
	@PostMapping
	public CustomResponseBody<SuccessStatusCode> saveShorts(
		@ModelAttribute @Valid @ApiParam(value = "쇼츠 등록 정보") ShortsDto.RequestDto requestDto) {
		shortsService.saveShorts(requestDto);
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_SHORTS);
	}

	// shorts 수정
	@ApiOperation(value = "쇼츠 수정 API")
	@PutMapping("/{shortsId}")
	public CustomResponseBody<SuccessStatusCode> updateShorts(
		@ModelAttribute @Valid @ApiParam(value = "쇼츠 수정 정보") ShortsDto.UpdateRequestDto requestDto,
		@PathVariable @ApiParam(value = "수정할 쇼츠 ID") Long shortsId) {
		shortsService.updateShorts(requestDto, shortsId);
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_SHORTS);
	}

	// shorts 삭제
	@ApiOperation(value = "쇼츠 삭제 API")
	@DeleteMapping("/{shortsId}")
	public CustomResponseBody<SuccessStatusCode> deleteShorts(
		@PathVariable @ApiParam(value = "삭제할 쇼츠 ID") Long shortsId) {
		shortsService.deleteShorts(shortsId);
		return new CustomResponseBody<>(SuccessStatusCode.DELETE_SHORTS);
	}

	// shorts 상세조회
	@ApiOperation(value = "쇼츠 상세조회 API")
	@GetMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> getShorts(
		@PathVariable @ApiParam(value = "조회할 쇼츠 ID") Long shortsId) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS, shortsService.getShorts(shortsId));
	}

	// shorts 화면에서 카테고리별 조회(최신순)
	@ApiOperation(value = "쇼츠 카테고리별 최신순 조회 API")
	@GetMapping
	public CustomResponseBody<List<ShortsDto.ResponseDto>> getShortsByCategory(
			@RequestParam(value = "category") @ApiParam(value = "조회할 카테고리") Category category,
			@RequestParam(value = "page") @ApiParam(value = "조회할 페이지") Long page) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_CATEGORY, shortsService.getShortsByCategory(category, page));
	}

	// 메인화면에서 카테고리별 조회
	@ApiOperation(value = "메인 화면에서 카테고리별 조회 API")
	@GetMapping("/main")
	public CustomResponseBody<List<ShortsDto.MainResponseDto>> getMainShortsByCategory(
		@RequestParam(value = "category") @ApiParam(value = "조회할 카테고리") Category category) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_CATEGORY, shortsService.getMainShortsByCategory(category));
	}

}