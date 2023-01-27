package com.team1.spreet.service;

import com.team1.spreet.dto.UserDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.jwt.JwtUtil;
import com.team1.spreet.repository.UserRepository;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

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
    public void updateUserInfo(UserDto.UpdateRequestDto requestDto, User user) {
        checkUser(user.getId());

        // 새로 변경을 원하는 닉네임이 중복인 경우
        if (!requestDto.getNickname().equals(user.getNickname()) &&
            userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
        }

        String profileImage;
        if (!requestDto.getProfileImage().isEmpty()) {
            //첨부파일 수정시 기존 첨부파일 삭제
            String fileName = user.getProfileImage().split(".com/")[1];
            awsS3Service.deleteFile(fileName);

            //새로운 파일 업로드
            profileImage = awsS3Service.uploadFile(requestDto.getProfileImage());
        } else {
            //첨부파일 수정 안함
            profileImage = user.getProfileImage();
        }
        user.updateUserInfo(requestDto.getNickname(), profileImage);
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