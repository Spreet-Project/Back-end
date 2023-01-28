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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "email")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class EmailController {

	private final EmailService emailService;

	@ApiOperation(value = "회원가입시 인증을 위해 이메일 전송하는 API")
	@PostMapping("/send-email")
	public CustomResponseBody<SuccessStatusCode> sendEmail(
		@RequestParam @ApiParam(value = "이메일 주소") String email) throws Exception {
		emailService.sendSimpleMessage(email);
		return new CustomResponseBody<>(SuccessStatusCode.EMAIL_SEND_SUCCESS);
	}

	@ApiOperation(value = "이메일 인증 API")
	@PostMapping("/confirm-email")
	public CustomResponseBody<SuccessStatusCode> emailConfirm(
		@RequestBody @ApiParam(value = "이메일 인증을 위한 정보") EmailDto emailDto) {
		emailService.emailConfirm(emailDto);
		return new CustomResponseBody<>(SuccessStatusCode.EMAIL_CONFIRM_SUCCESS);
	}

}
