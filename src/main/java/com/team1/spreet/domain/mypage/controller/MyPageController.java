package com.team1.spreet.domain.mypage.controller;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.mypage.service.MyPageService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/mypage")
public class MyPageController {

	private final MyPageService myPageService;

	// 마이페이지 회원정보 조회
	@ApiOperation(value = "회원정보 조회 API")
	@GetMapping
	public CustomResponseBody<MyPageDto.UserInfoResponseDto> getUserInfo() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_USER_INFO, myPageService.getUserInfo());
	}

	// 프로필 사진 변경
	@ApiOperation(value = "프로필 이미지 수정 API")
	@PutMapping("/edit/profile-image")
	public CustomResponseBody<SuccessStatusCode> updateProfileImage(
		@RequestParam(value = "file") @Valid @ApiParam(value = "새로운 프로필 이미지") MultipartFile file) {
		myPageService.updateProfileImage(file);
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_USER_INFO);
	}

	// 닉네임 변경
	@ApiOperation(value = "닉네임 수정 API")
	@PutMapping("/edit/nickname")
	public CustomResponseBody<SuccessStatusCode> updateNickname(
		@RequestBody @Valid @ApiParam(value = "새로운 닉네임") MyPageDto.NicknameRequestDto requestDto) {
		myPageService.updateNickname(requestDto);
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_USER_INFO);
	}

	// 비밀번호 변경
	@ApiOperation(value = "회원 비밀번호 수정 API")
	@PutMapping("/edit/password")
	public CustomResponseBody<SuccessStatusCode> updatePassword(
		@RequestBody @Valid @ApiParam(value = "수정할 비밀번호") MyPageDto.PasswordRequestDto requestDto) {
		myPageService.updatePassword(requestDto);
		return new CustomResponseBody<>(SuccessStatusCode.UPDATE_PASSWORD);
	}

	// 비밀번호 변경시 인증용 이메일 전송
	@ApiOperation(value = "비밀번호 변경시 인증을 위해 이메일 전송하는 API")
	@PostMapping("/send-email")
	public CustomResponseBody<SuccessStatusCode> sendConfirmEmail(
		@RequestParam @ApiParam(value = "이메일 주소") String email) throws Exception {
		myPageService.sendConfirmEmail(email);
		return new CustomResponseBody<>(SuccessStatusCode.EMAIL_SEND_SUCCESS);
	}

	// 회원이 작성한 게시글 목록 조회
	@ApiOperation(value = "회원이 작성한 게시글 조회 API")
	@GetMapping("/post")
	public CustomResponseBody<List<MyPageDto.PostResponseDto>> getPostList (
		@RequestParam(value = "classification") @ApiParam(value = "게시글 분류") String classification,
		@RequestParam(value = "page") @ApiParam(value = "조회할 페이지") Long page) {
		return new CustomResponseBody<>(SuccessStatusCode.GET_POST_LIST,
			myPageService.getPostList(classification, page));
	}

}
