package com.team1.spreet.domain.user.controller;

import com.team1.spreet.domain.user.dto.UserDto;
import com.team1.spreet.domain.user.service.UserService;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "user")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입 API")
    @PostMapping("/signup")
    public CustomResponseBody<SuccessStatusCode> signup(@RequestBody @Valid @ApiParam(value = "회원 가입할 회원 정보") final UserDto.SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.SIGNUP_SUCCESS);
    }

    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public CustomResponseBody<UserDto.LoginResponseDto> login(@RequestBody @ApiParam(value = "로그인 정보") final UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
        return new CustomResponseBody<>(SuccessStatusCode.LOGIN_SUCCESS ,userService.login(requestDto, httpServletResponse));
    }

    @ApiOperation(value = "아이디 중복확인 API")
    @PostMapping("/id-check")
    public CustomResponseBody<SuccessStatusCode> idCheck(@RequestParam @ApiParam(value = "중복확인 할 아이디") String loginId) {
        userService.idCheck(loginId);
        return new CustomResponseBody<>(SuccessStatusCode.ID_DUPLICATE_CHECK);
    }

    @ApiOperation(value = "닉네임 중복확인 API")
    @PostMapping("/nickname-check")
    public CustomResponseBody<SuccessStatusCode> nicknameCheck(@RequestParam @ApiParam(value = "중복확인 할 닉네임") String nickname) {
        userService.nicknameCheck(nickname);
        return new CustomResponseBody<>(SuccessStatusCode.NICKNAME_DUPLICATE_CHECK);
    }

    @ApiOperation(value = "비밀번호 재설정 API")
    @PutMapping("/reset/password")
    public CustomResponseBody<SuccessStatusCode> resetPassword(
        @RequestBody @Valid @ApiParam(value = "비밀번호 재설정을 위한 정보") UserDto.ResetPwRequestDto requestDto) {
        userService.resetPassword(requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_PASSWORD);
    }

    @ApiOperation(value = "회원탈퇴 API")
    @PostMapping("/quit")
    public CustomResponseBody<SuccessStatusCode> userWithdraw(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String password) {
        userService.userWithdraw(password, userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.WITHDRAW_SUCCESS);
    }
}