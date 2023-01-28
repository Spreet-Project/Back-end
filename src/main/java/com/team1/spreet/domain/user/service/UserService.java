package com.team1.spreet.domain.user.service;

import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.event.repository.EventRepository;
import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.repository.FeedRepository;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.repository.ShortsRepository;
import com.team1.spreet.domain.user.dto.UserDto;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.auth.jwt.JwtUtil;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.s3.service.AwsS3Service;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final EventRepository eventRepository;

    private final FeedRepository feedRepository;

    private final ShortsRepository shortsRepository;

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service awsS3Service;

    public void signup(final UserDto.SignupRequestDto requestDto) {
        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
        } else if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
        } else if (!requestDto.isEmailConfirm()) {
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_EXCEPTION);
        }

        userRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword())));
    }

    public void userWithdraw( String password, User user) {
        if (passwordEncoder.encode(password).equals(user.getPassword())) {
            userRepository.updateIsDeletedTrueByLoginId(user.getLoginId());
        } else {
            throw new RestApiException(ErrorStatusCode.PASSWORD_CONFIRM_INCORRECT);
        }
    }

    public UserDto.LoginResponseDto login(UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {

        // 크루 승인 대기 중인 유저는 로그인 불가
        if (userRepository.findByLoginIdAndUserRoleAndIsDeletedFalse(requestDto.getLoginId(), UserRole.ROLE_UNACCEPTED_CREW).isPresent()) {
            throw new RestApiException(ErrorStatusCode.WAITING_CREW_APPROVAL);
        }

        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());

        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        String token = jwtUtil.createToken(afterAuthentication);

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return getNickname(requestDto);
    }

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public UserDto.UserInfoResponseDto getUserInfo(User user) {
        checkUser(user.getId());

        return new UserDto.UserInfoResponseDto(user.getLoginId(), user.getNickname(),
            user.getEmail(), user.getProfileImage());
    }

    // 회원 정보 수정
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

    // 회원 정보 수정
    public void updateNickname(UserDto.NicknameRequestDto requestDto, User user) {
        checkUser(user.getId());

        // 변경하려는 닉네임이 중복인 경우
        if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
        }
        user.updateNickname(requestDto.getNickname());
        userRepository.saveAndFlush(user);
    }

    // 비밀번호 초기화(변경)
    public void resetPassword(UserDto.ResetPwRequestDto requestDto, User user) {
        checkUser(user.getId());

        // 기존과 동일한 비밀번호의 경우 에러처리
        if (user.getPassword().equals(passwordEncoder.encode(requestDto.getPassword()))) {
            throw new RestApiException(ErrorStatusCode.INVALID_PASSWORD);
        }
        user.resetPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.saveAndFlush(user);
    }

    // 회원이 작성한 게시글 목록(쇼츠,피드,행사) 조회
    @Transactional(readOnly = true)
    public List<UserDto.PostResponseDto> getPostList(String classification, int page, User user) {
        checkUser(user.getId());

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<UserDto.PostResponseDto> postList = new ArrayList<>();
        // shorts
        if (classification.equals("shorts")) {
            List<Shorts> shortsList = shortsRepository.findAllByUserIdAndIsDeletedFalse(user.getId(), pageable);
            for (Shorts shorts : shortsList) {
                postList.add(new UserDto.PostResponseDto(classification, shorts.getId(), shorts.getTitle(),
                    shorts.getCategory().value(), shorts.getCreatedAt()));
            }
        }

        // feed
        if (classification.equals("feed")) {
            List<Feed> feedList = feedRepository.findAllByUserIdAndIsDeletedFalse(user.getId(), pageable);
            for (Feed feed : feedList) {
                postList.add(new UserDto.PostResponseDto(classification, feed.getId(), feed.getTitle(), feed.getCreatedAt()));
            }
        }

        // event
        if (classification.equals("event")) {
            List<Event> eventList = eventRepository.findAllByUserIdAndIsDeletedFalse(user.getId(), pageable);
            for (Event event : eventList) {
                postList.add(new UserDto.PostResponseDto(classification, event.getId(), event.getTitle(), event.getCreatedAt()));
            }
        }
        return postList;
    }

    public void idCheck(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
    }

    public void nicknameCheck(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
    }

    private void checkUser(Long userId) {
        if (userRepository.findByIdAndIsDeletedFalse(userId).isEmpty()) {
            throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
        }
    }

    private UserDto.LoginResponseDto getNickname(UserDto.LoginRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        return new UserDto.LoginResponseDto(user.getNickname());
    }
}