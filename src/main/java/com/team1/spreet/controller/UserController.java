package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.UserRepository;
import com.team1.spreet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "user")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입 API")
    @PostMapping("/signup")
    public CustomResponseBody signup(@RequestBody @Valid @ApiParam(value = "회원 가입할 회원 정보") final UserDto.SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.SIGNUP_SUCCESS);
    }

    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public CustomResponseBody login(@RequestBody @ApiParam(value = "로그인 정보") final UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
        return new CustomResponseBody<>(SuccessStatusCode.LOGIN_SUCCESS ,userService.login(requestDto, httpServletResponse));
    }

    @ApiOperation(value = "아이디 중복확인 API")
    @PostMapping("/id-check")
    public CustomResponseBody idCheck(@RequestParam @ApiParam(value = "중복확인 할 아이디") String loginId) {
        userService.idCheck(loginId);
        return new CustomResponseBody<>(SuccessStatusCode.ID_DUPLICATE_CHECK);
    }

    @ApiOperation(value = "닉네임 중복확인 API")
    @PostMapping("/nickname-check")
    public CustomResponseBody nicknameCheck(@RequestParam @ApiParam(value = "중복확인 할 닉네임") String nickname) {
        userService.nicknameCheck(nickname);
        return new CustomResponseBody<>(SuccessStatusCode.NICKNAME_DUPLICATE_CHECK);
    }
}