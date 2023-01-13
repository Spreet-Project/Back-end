package com.team1.spreet.controller;

import com.team1.spreet.entity.EmailConfirm;
import com.team1.spreet.repository.EmailConfirmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;


@Controller
public class EmailConfirmController {

    @Autowired
    EmailConfirmRepository emailConfirmRepository;

    @Cacheable(value = "EmailConfirm")
    public EmailConfirm findEmailConfirmById(String id) {
        return emailConfirmRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
