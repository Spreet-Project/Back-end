package com.team1.spreet.domain.user.service;

import com.team1.spreet.domain.user.dto.UserDto;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.auth.jwt.JwtUtil;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.infra.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    private final EmailService emailService;

    //회원 가입
    public void signup(final UserDto.SignupRequestDto requestDto) {
        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
        } else if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
        } else if (!requestDto.isEmailConfirm()) {
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_EXCEPTION);
        }

        userRepository.save(requestDto.toEntity(bcryptPasswordEncoder.encode(requestDto.getPassword())));
    }

    // 회원 탈퇴
    public void userWithdraw(String password, User user) {
        if (bcryptPasswordEncoder.matches(password, user.getPassword())) {
            user.deleteUser();
            userRepository.saveAndFlush(user);
        } else {
            throw new RestApiException(ErrorStatusCode.PASSWORD_CONFIRM_INCORRECT);
        }
    }

    // 로그인
    public UserDto.LoginResponseDto login(UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {

        // 크루 승인 대기 중인 유저는 로그인 불가
        if (userRepository.findByLoginIdAndUserRoleAndDeletedFalse(requestDto.getLoginId(), UserRole.ROLE_WAITING_CREW).isPresent()) {
            throw new RestApiException(ErrorStatusCode.WAITING_CREW_APPROVAL);
        }

        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());

        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        String token = jwtUtil.createToken(afterAuthentication);

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return getNickname(requestDto);
    }

    // 로그인 화면에서 비밀번호를 잊은 경우 재설정
    public void resetPassword(UserDto.ResetPwRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.MISMATCH_EMAIL)
        );

        if (!requestDto.isEmailConfirm())
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_EXCEPTION);

        if(bcryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new RestApiException(ErrorStatusCode.INVALID_PASSWORD);

        user.updatePassword(bcryptPasswordEncoder.encode(requestDto.getPassword()));
        userRepository.saveAndFlush(user);
    }

    // 회원가입 시 이메일 인증
    public void signupSendEmail(String email) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_EMAIL);
        }
        emailService.sendSimpleMessage(email);
    }

    // 비밀번호 변경 시 이메일 인증
    public void resetPasswordSendEmail(String email) throws Exception {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new RestApiException(ErrorStatusCode.EMAIL_CONFIRM_NULL_EXCEPTION);
        }
        emailService.sendSimpleMessage(email);
    }

    // 아이디 중복 확인
    public void idCheck(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
    }

    // 닉네임 중복 확인
    public void nicknameCheck(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
    }

    // 닉네임 찾아오기
    private UserDto.LoginResponseDto getNickname(UserDto.LoginRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        return new UserDto.LoginResponseDto(user.getNickname(), user.getUserRole());
    }
}