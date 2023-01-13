package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.EmailDto;
import com.team1.spreet.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "email")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @ApiOperation(value = "이메일 전송 API")
    @PostMapping("/api/user/send-email")
    public CustomResponseBody sendEmail(@RequestParam @ApiParam(value = "이메일 주소") String email) throws Exception {
        return emailService.sendSimpleMessage(email);
    }

    @ApiOperation(value = "이메일 인증 API")
    @GetMapping("/api/user/confirm-email")
    public CustomResponseBody emailConfirm(@RequestBody @ApiParam(value = "이메일 인증을 위한 정보") EmailDto emailDto) throws Exception{
        return emailService.emailConfirm(emailDto);
    }
}
