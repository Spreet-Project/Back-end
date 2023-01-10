package com.team1.spreet.controller;

import com.team1.spreet.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/api/user/emailConfirm")
    public String emailConfirm(@RequestParam String email) throws Exception {
        return emailService.sendSimpleMessage(email);
    }
}
