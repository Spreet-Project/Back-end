package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.ShortsLikeDto;
import com.team1.spreet.service.ShortsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shorts/like")
public class ShortsLikeController {
	private final ShortsLikeService shortsLikeService;

	// shortsLike 등록
	@PostMapping("/{shortsId}")
	public CustomResponseBody<ShortsLikeDto.ResponseDto> setShortsLike(@PathVariable Long shortsId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return shortsLikeService.setShortsLike(shortsId, Long.parseLong(userDetails.getUsername()));
	}
}