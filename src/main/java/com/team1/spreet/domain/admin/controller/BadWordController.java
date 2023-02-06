package com.team1.spreet.domain.admin.controller;

import com.team1.spreet.domain.admin.dto.BadWordDto;
import com.team1.spreet.domain.admin.service.BadWordService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class BadWordController {

	private final BadWordService badWordService;

	// 비속어 DB 저장
	@PostMapping("/new/bad-word")
	public CustomResponseBody<SuccessStatusCode> createBadWord(@RequestBody BadWordDto.RequestDto requestDto) {
		badWordService.createBadWord(requestDto);
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_BAD_WORD);
	}

	// 비속어 레디스 저장
	@PostMapping("/bad-word")
	public CustomResponseBody<SuccessStatusCode> addBadWord() {
		badWordService.addBadWord();
		return new CustomResponseBody<>(SuccessStatusCode.SAVE_BAD_WORD);
	}

}
