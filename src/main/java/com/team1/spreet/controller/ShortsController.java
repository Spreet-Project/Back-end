package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsDto;
import com.team1.spreet.entity.Category;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.ShortsService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsController {

	private final ShortsService shortsService;

	// shorts 등록
	@PostMapping
	public CustomResponseBody<ShortsDto.ResponseDto> saveShorts(@ModelAttribute @Valid ShortsDto.RequestDto requestDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsService.saveShorts(requestDto, Long.parseLong(userDetails.getUsername())));
	}

	// shorts 수정
	@PutMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> updateShorts(@ModelAttribute ShortsDto.UpdateRequestDto requestDto,
		@PathVariable Long shortsId, @AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsService.updateShorts(requestDto, shortsId, Long.parseLong(userDetails.getUsername())));
	}

	// shorts 삭제
	@DeleteMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> deleteShorts(@PathVariable Long shortsId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return new CustomResponseBody<>(shortsService.deleteShorts(shortsId, Long.parseLong(userDetails.getUsername())));
	}

	// shorts 상세조회
	@GetMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> getShorts(@PathVariable Long shortsId,
		@AuthenticationPrincipal UserDetails userDetails) {
		Long userId = userDetails == null ? 0L : Long.parseLong(userDetails.getUsername());
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS, shortsService.getShorts(shortsId, userId));
	}

	// shorts 카테고리별 조회
	@GetMapping
	public CustomResponseBody<List<ShortsDto.ResponseDto>> getShortsByCategory
		(@RequestParam(value = "category") Category category, @RequestParam(value = "page") int page,
			@RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserDetails userDetails) {
		Long userId = userDetails == null ? 0L : Long.parseLong(userDetails.getUsername());
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_CATEGORY, shortsService.getShortsByCategory(category, page, size, userId));
	}

	// 모든 카테고리 최신 shorts 10개씩 조회
	@GetMapping("/category")
	public CustomResponseBody<ShortsDto.CategoryResponseDto> getAllCategory() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_ALL_CATEGORY, shortsService.getAllCategory());
	}

}