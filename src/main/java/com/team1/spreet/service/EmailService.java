package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.EmailDto;

public interface EmailService {
    CustomResponseBody sendSimpleMessage(String email) throws Exception;

    CustomResponseBody emailConfirm(EmailDto emailDto) throws Exception;
}
