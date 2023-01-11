package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.EmailDto;
import com.team1.spreet.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/api/user/send-email")
    public CustomResponseBody sendEmail(@RequestParam String email) throws Exception {
        return emailService.sendSimpleMessage(email);
    }

    @GetMapping("/api/user/confirm-email")
    public CustomResponseBody emailConfirm(@RequestBody EmailDto emailDto) throws Exception{
        return emailService.emailConfirm(emailDto);
    }
}
