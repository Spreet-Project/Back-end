package com.team1.spreet.global.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class deployController {

    @GetMapping("/health")
    public String checkHealth(){
        return "healthy";
    }
}
