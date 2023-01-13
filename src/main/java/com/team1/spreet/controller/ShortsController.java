package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsDto;
import com.team1.spreet.entity.Category;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.ShortsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "shorts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsController {

	private final ShortsService shortsService;

	// shorts 등록
	@ApiOperation(value = "쇼츠 등록 API")
	@PostMapping
	public CustomResponseBody<ShortsDto.ResponseDto> saveShorts(@ModelAttribute @Valid @ApiParam(value = "쇼츠 등록 정보") ShortsDto.RequestDto requestDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsService.saveShorts(requestDto, Long.parseLong(userDetails.getUsername())));
	}

	// shorts 수정
	@ApiOperation(value = "쇼츠 수정 API")
	@PutMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> updateShorts(@ModelAttribute @Valid @ApiParam(value = "쇼츠 수정 정보") ShortsDto.UpdateRequestDto requestDto,
		@PathVariable @ApiParam(value = "수정할 쇼츠 ID") Long shortsId, @AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsService.updateShorts(requestDto, shortsId, Long.parseLong(userDetails.getUsername())));
	}

	// shorts 삭제
	@ApiOperation(value = "쇼츠 삭제 API")
	@DeleteMapping("/{shortsId}")
	public CustomResponseBody<SuccessStatusCode> deleteShorts(@PathVariable @ApiParam(value = "삭제할 쇼츠 ID") Long shortsId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsService.deleteShorts(shortsId, Long.parseLong(userDetails.getUsername())));
	}

	// shorts 상세조회
	@ApiOperation(value = "쇼츠 상세조회 API")
	@GetMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> getShorts(@PathVariable @ApiParam(value = "조회할 쇼츠 ID") Long shortsId,
		@AuthenticationPrincipal UserDetails userDetails) {
		Long userId = userDetails == null ? 0L : Long.parseLong(userDetails.getUsername());
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS, shortsService.getShorts(shortsId, userId));
	}

	// shorts 카테고리별 조회
	@ApiOperation(value = "쇼츠 카테고리별 조회 API")
	@GetMapping
	public CustomResponseBody<List<ShortsDto.ResponseDto>> getShortsByCategory
		(@RequestParam(value = "category") @ApiParam(value = "조회할 카테고리") Category category,
			@RequestParam(value = "page") @ApiParam(value = "조회할 페이지") int page,
			@RequestParam(defaultValue = "10") @ApiParam(value = "조회할 사이즈") int size, @AuthenticationPrincipal UserDetails userDetails) {
		Long userId = userDetails == null ? 0L : Long.parseLong(userDetails.getUsername());
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_CATEGORY, shortsService.getShortsByCategory(category, page, size, userId));
	}

	// 모든 카테고리 최신 shorts 10개씩 조회
	@ApiOperation(value = "쇼츠 모든 카테고리 조회 API")
	@GetMapping("/category")
	public CustomResponseBody<ShortsDto.CategoryResponseDto> getAllCategory() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_ALL_CATEGORY, shortsService.getAllCategory());
	}

}