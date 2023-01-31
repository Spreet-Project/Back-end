package com.team1.spreet.domain.mypage.service;

import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.mypage.dto.MyPageDto;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.email.service.EmailService;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AwsS3Service awsS3Service;
	private final ShortsRepository shortsRepository;
	private final FeedRepository feedRepository;
	private final EventRepository eventRepository;
	private final EmailService emailService;


	// 회원 정보 조회
	@Transactional(readOnly = true)
	public MyPageDto.UserInfoResponseDto getUserInfo(User user) {
		checkUser(user.getId());

		return new MyPageDto.UserInfoResponseDto(user.getLoginId(), user.getNickname(),
			user.getEmail(), user.getProfileImage());
	}

	// 프로필 사진 변경
	public void updateProfileImage(MultipartFile file, User user) {
		checkUser(user.getId());

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
	public void updateNickname(MyPageDto.NicknameRequestDto requestDto, User user) {
		checkUser(user.getId());

		// 변경하려는 닉네임이 중복인 경우
		if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
			throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
		}
		user.updateNickname(requestDto.getNickname());
		userRepository.saveAndFlush(user);
	}

	// 비밀번호 초기화(변경)
	public void updatePassword(MyPageDto.PasswordRequestDto requestDto, User user) {
		checkUser(user.getId());

		// 기존과 동일한 비밀번호의 경우 에러처리
		if (user.getPassword().equals(passwordEncoder.encode(requestDto.getPassword()))) {
			throw new RestApiException(ErrorStatusCode.INVALID_PASSWORD);
		}
		user.updatePassword(passwordEncoder.encode(requestDto.getPassword()));
		userRepository.saveAndFlush(user);
	}

	// 비밀번호 변경시 인증 위해 이메일 전송
	public void sendConfirmEmail(String email, User user) throws Exception {
		if (!email.equals(user.getEmail())) {
			throw new RestApiException(ErrorStatusCode.MISMATCH_EMAIL);
		}
		emailService.sendSimpleMessage(email);
	}

	// 회원이 작성한 게시글 목록(쇼츠,피드,행사) 조회
	@Transactional(readOnly = true)
	@Cacheable(key = "#page.toString() + #classification + #user.getId()", value = "shorts")
	public List<MyPageDto.PostResponseDto> getPostList(String classification, int page, User user) {
		checkUser(user.getId());

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<MyPageDto.PostResponseDto> postList = new ArrayList<>();
		// shorts
		if (classification.equals("shorts")) {
			List<Shorts> shortsList = shortsRepository.findAllByUserIdAndIsDeletedFalse(user.getId(), pageable);
			for (Shorts shorts : shortsList) {
				postList.add(new MyPageDto.PostResponseDto(classification, shorts.getId(), shorts.getTitle(),
					shorts.getCategory().value(), shorts.getCreatedAt()));
			}
		}

		// feed
		if (classification.equals("feed")) {
			List<Feed> feedList = feedRepository.findAllByUserIdAndDeletedFalse(user.getId(), pageable);
			for (Feed feed : feedList) {
				postList.add(new MyPageDto.PostResponseDto(classification, feed.getId(), feed.getTitle(), feed.getCreatedAt()));
			}
		}

		// event
		if (classification.equals("event")) {
			List<Event> eventList = eventRepository.findAllByUserIdAndDeletedFalse(user.getId(), pageable);
			for (Event event : eventList) {
				postList.add(new MyPageDto.PostResponseDto(classification, event.getId(), event.getTitle(), event.getCreatedAt()));
			}
		}
		return postList;
	}

	private void checkUser(Long userId) {
		if (userRepository.findByIdAndDeletedFalse(userId).isEmpty()) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}
	}

}
