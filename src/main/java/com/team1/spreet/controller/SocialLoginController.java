package com.team1.spreet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.NaverLoginService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SocialLoginController {
	private final NaverLoginService naverLoginService;

	@ApiOperation(value = "네이버 로그인 API")
	@GetMapping("/naver/callback")
	public CustomResponseBody<UserDto> naverLogin(@RequestParam @ApiParam(value = "인가 코드") String code,
		@RequestParam @ApiParam(value = "상태") String state, HttpServletResponse response) throws JsonProcessingException {
		return new CustomResponseBody<>(SuccessStatusCode.LOGIN_SUCCESS, naverLoginService.naverLogin(code, state, response));
	}

}
