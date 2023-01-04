package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public CustomResponseBody signup(@RequestBody @Valid final UserDto.SignupRequestDto requestDto) {
        log.info("requestDto : " + requestDto);
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public CustomResponseBody login(@RequestBody final UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
        return userService.login(requestDto, httpServletResponse);
    }

    //소셜 로그인 추가 예정!
}
