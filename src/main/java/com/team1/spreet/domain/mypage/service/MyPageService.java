package com.team1.spreet.domain.mypage.service;

import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.email.service.EmailService;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import com.team1.spreet.global.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bcryptPasswordEncoder;
	private final AwsS3Service awsS3Service;
	private final ShortsRepository shortsRepository;
	private final FeedRepository feedRepository;
	private final EventRepository eventRepository;
	private final EmailService emailService;


	// 회원 정보 조회
	@Transactional(readOnly = true)
	public MyPageDto.UserInfoResponseDto getUserInfo() {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}

		return new MyPageDto.UserInfoResponseDto(user.getLoginId(), user.getNickname(),
			user.getEmail(), user.getProfileImage());
	}

	// 프로필 사진 변경
	public void updateProfileImage(MultipartFile file) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}
		String profileImage;
		if (user.getProfileImage().contains("https://spreet")) {
			//첨부파일 수정시 기존 첨부파일 삭제
			String fileName = user.getProfileImage().split(".com/")[1];
			awsS3Service.deleteFile(fileName);
		}
		profileImage = awsS3Service.uploadFile(file);
		user.updateProfileImage(profileImage);
		userRepository.saveAndFlush(user);
	}

	// 닉네임 변경
	public void updateNickname(MyPageDto.NicknameRequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}

		// 변경하려는 닉네임이 중복인 경우
		if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
			throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
		}
		user.updateNickname(requestDto.getNickname());
		userRepository.saveAndFlush(user);
	}

	// 비밀번호 초기화(변경)
	public void updatePassword(MyPageDto.PasswordRequestDto requestDto) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}

		// 기존과 동일한 비밀번호의 경우 에러처리
		if (user.getPassword().equals(bcryptPasswordEncoder.encode(requestDto.getPassword()))) {
			throw new RestApiException(ErrorStatusCode.INVALID_PASSWORD);
		}
		user.updatePassword(bcryptPasswordEncoder.encode(requestDto.getPassword()));
		userRepository.saveAndFlush(user);
	}

	// 비밀번호 변경시 인증 위해 이메일 전송
	public void sendConfirmEmail(String email) throws Exception {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}

		if (!email.equals(user.getEmail())) {
			throw new RestApiException(ErrorStatusCode.MISMATCH_EMAIL);
		}
		emailService.sendSimpleMessage(email);
	}

	// 회원이 작성한 게시글 목록(쇼츠,피드,행사) 조회
	@Transactional(readOnly = true)
	public List<MyPageDto.PostResponseDto> getPostList(String classification, Long page) {
		User user = SecurityUtil.getCurrentUser();
		if (user == null) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}

		// shorts
		if (classification.equals("shorts")) {
			return shortsRepository.findByUserId(classification, page - 1, user.getId());
		}

		// feed
		if (classification.equals("feed")) {
			return feedRepository.findByUserId(classification, page - 1, user.getId());
		}

		// event
		if (classification.equals("event")) {
			return eventRepository.findByUserId(classification, page - 1, user.getId());
		}
		return null;
	}

}
