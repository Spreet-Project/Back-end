package com.team1.spreet.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.domain.user.dto.UserDto;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.domain.user.service.KakaoLoginService;
import com.team1.spreet.domain.user.service.NaverLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "socialLogin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SocialLoginController {
	private final NaverLoginService naverLoginService;
	private final KakaoLoginService kakaoLoginService;

	@ApiOperation(value = "네이버 로그인 API")
	@GetMapping("/naver/callback")
	public CustomResponseBody<UserDto.LoginResponseDto> naverLogin(@RequestParam @ApiParam(value = "인가 코드") String code,
		@RequestParam @ApiParam(value = "상태") String state, HttpServletResponse response) throws JsonProcessingException {
		return new CustomResponseBody<>(SuccessStatusCode.LOGIN_SUCCESS, naverLoginService.naverLogin(code, state, response));
	}

	@ApiOperation(value = "카카오 로그인 API")
	@GetMapping("/kakao/callback")
	public CustomResponseBody<UserDto.LoginResponseDto> kakaoLogin(@RequestParam @ApiParam(value = "로그인 코드") String code, HttpServletResponse httpServletResponse) throws JsonProcessingException {
		return new CustomResponseBody<>(SuccessStatusCode.LOGIN_SUCCESS, kakaoLoginService.kakaoLogin(code, httpServletResponse));
	}

}
