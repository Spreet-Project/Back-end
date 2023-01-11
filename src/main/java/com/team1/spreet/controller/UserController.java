package com.team1.spreet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    //local signup
    @PostMapping("/signup")
    public CustomResponseBody signup(@RequestBody @Valid final UserDto.SignupRequestDto requestDto) {
        log.info("requestDto : " + requestDto);
        return userService.signup(requestDto);
    }

    //local login
    @PostMapping("/login")
    public CustomResponseBody login(@RequestBody final UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
        return userService.login(requestDto, httpServletResponse);
    }

    //kakao login
    @GetMapping("/kakao/callback")
    public CustomResponseBody kakaoLogin(@RequestParam String code, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        return userService.kakaoLogin(code, httpServletResponse);
    }

    @PostMapping("/id-check")
    public CustomResponseBody idCheck(@RequestParam String loginId) {
        return userService.idCheck(loginId);
    }

    @PostMapping("/nickname-check")
    public CustomResponseBody nicknameCheck(@RequestParam String nickname) {
        return userService.nicknameCheck(nickname);
    }
}
