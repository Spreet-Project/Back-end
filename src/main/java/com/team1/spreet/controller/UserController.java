package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation(value = "회원정보 조회 API")
    @GetMapping("/mypage")
    public CustomResponseBody<UserDto.UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(SuccessStatusCode.GET_USER_INFO, userService.getUserInfo(userDetails.getUser()));
    }

    @ApiOperation(value = "프로필 이미지 수정 API")
    @PutMapping("/mypage/edit/profile-image")
    public CustomResponseBody<SuccessStatusCode> updateProfileImage(
        @RequestParam(value = "file") @Valid @ApiParam(value = "새로운 프로필 이미지") MultipartFile file,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateProfileImage(file, userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_USER_INFO);
    }

    @ApiOperation(value = "회원정보 수정 API")
    @PutMapping("/mypage/edit/nickname")
    public CustomResponseBody<SuccessStatusCode> updateNickname(
        @RequestBody @Valid @ApiParam(value = "새로운 닉네임") UserDto.NicknameRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateNickname(requestDto, userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_USER_INFO);
    }

    // 회원이 작성한 게시글 목록 조회
    @ApiOperation(value = "회원이 작성한 게시글 조회 API")
    @GetMapping("/mypage/post")
    public CustomResponseBody<List<UserDto.PostResponseDto>> getPostList (
        @RequestParam(value = "classification") @ApiParam(value = "게시글 분류") String classification,
        @RequestParam(value = "page") @ApiParam(value = "조회할 페이지") int page,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(SuccessStatusCode.GET_POST_LIST, userService.getPostList(classification, page, userDetails.getUser()));
    }

    @ApiOperation(value = "회원 비밀번호 수정 API")
    @PutMapping("/reset/password")
    public CustomResponseBody<SuccessStatusCode> resetPassword(
        @RequestBody @Valid @ApiParam(value = "수정할 비밀번호") UserDto.ResetPwRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.resetPassword(requestDto, userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_PASSWORD);
    }

    @ApiOperation(value = "회원탈퇴 API")
    @PostMapping("/quit")
    public CustomResponseBody<SuccessStatusCode> userWithdraw(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String password) {
        userService.userWithdraw(password, userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.WITHDRAW_SUCCESS);
    }
}