package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.ShortsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts")
public class ShortsController {

	private final ShortsService shortsService;

	@PostMapping
	public CustomResponseBody<ShortsDto.ResponseDto> saveShorts(@RequestBody ShortsDto.RequestDto requestDto) {
		return new CustomResponseBody<>(shortsService.saveShorts(requestDto));
	}

	@PutMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> updateShorts(@RequestBody ShortsDto.RequestDto requestDto, @PathVariable Long shortsId) {
		return new CustomResponseBody<>(shortsService.updateShorts(requestDto, shortsId));
	}

	@GetMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> getShorts(@PathVariable Long shortsId) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS, shortsService.getShorts(shortsId));
	}

	@GetMapping
	public CustomResponseBody<List<ShortsDto.ResponseDto>> getShortsByCategory
		(@RequestParam(value = "category") String category, @RequestParam(value = "page") int page,
			@RequestParam(defaultValue = "10") int size, Pageable pageable) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_SHORTS_BY_CATEGORY, shortsService.getShortsByCategory(category, page, size, pageable));
	}

	@DeleteMapping("/{shortsId}")
	public CustomResponseBody<ShortsDto.ResponseDto> deleteShorts(@PathVariable Long shortsId) {
		return new CustomResponseBody<>(shortsService.deleteShorts(shortsId));
	}
}
