package com.team1.spreet.service;

import com.team1.spreet.dto.UserDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.jwt.JwtUtil;
import com.team1.spreet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public void signup(final UserDto.SignupRequestDto requestDto) {
        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
        } else if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
        }

        userRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword()), UserRole.ROLE_USER));
    }

    public void userWithdraw( String password, User user) {
        if (passwordEncoder.encode(password).equals(user.getPassword())) {
            userRepository.updateIsDeletedTrueByLoginId(user.getLoginId());
        } else {
            throw new RestApiException(ErrorStatusCode.PASSWORD_CONFIRM_INCORRECT);
        }
    }

    //로그인
    public UserDto.LoginResponseDto login(UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {

        // 크루 승인 대기 중인 유저는 로그인 불가
        if (userRepository.existsByLoginIdAndUserRoleAndIsCrew(requestDto.getLoginId(), UserRole.ROLE_CREW, false)) {
            throw new RestApiException(ErrorStatusCode.WAITING_CREW_APPROVAL);
        }

        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());

        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        String token = jwtUtil.createToken(afterAuthentication);

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return getNickname(requestDto);
    }

    //아이디 중복 확인
    public void idCheck(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_ID);
    }

    //닉네임 중복 확인
    public void nicknameCheck(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new RestApiException(ErrorStatusCode.OVERLAPPED_NICKNAME);
    }

    //로그인 아이디로 닉네임 받아오기
    private UserDto.LoginResponseDto getNickname(UserDto.LoginRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        return new UserDto.LoginResponseDto(user.getNickname());
    }
}