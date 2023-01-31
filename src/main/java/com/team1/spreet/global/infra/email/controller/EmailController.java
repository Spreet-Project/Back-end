package com.team1.spreet.global.infra.email.controller;

import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.infra.email.dto.EmailDto;
import com.team1.spreet.global.infra.email.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "email")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailController {

	private final EmailService emailService;

	@ApiOperation(value = "이메일 인증 API")
	@PostMapping("/confirm-email")
	public CustomResponseBody<SuccessStatusCode> emailConfirm(
		@RequestBody @ApiParam(value = "이메일 인증을 위한 정보") EmailDto emailDto) {
		emailService.emailConfirm(emailDto);
		return new CustomResponseBody<>(SuccessStatusCode.EMAIL_CONFIRM_SUCCESS);
	}
}