package com.team1.spreet.service;

import com.team1.spreet.dto.EmailDto;

public interface EmailService {
    void sendSimpleMessage(String email) throws Exception;

    void emailConfirm(EmailDto emailDto) throws Exception;
}
