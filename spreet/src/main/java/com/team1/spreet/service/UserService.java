package com.team1.spreet.service;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
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

    public CustomResponseBody signup(final UserDto.SignupRequestDto requestDto) {
        User user = userRepository.findByNickname(requestDto.getNickname()).orElse(null);

        if(userRepository.existsByLoginIdAndIsDeleted(requestDto.getLoginId(), true))
            throw new RestApiException(ErrorStatusCode.DELETED_ACCOUNT_EXCEPTION);

        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.ID_ALREADY_EXISTS_EXCEPTION);
        } else if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new RestApiException(ErrorStatusCode.NICKNAME_ALREADY_EXISTS_EXCEPTION);
        }


        userRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword())));
        return new CustomResponseBody(SuccessStatusCode.SIGNUP_SUCCESS);
    }

    public CustomResponseBody login(UserDto.LoginRequestDto requestDto, HttpServletResponse httpServletResponse) {
        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword());

        Authentication afterAuthentication = authenticationManagerBuilder.getObject().authenticate(beforeAuthentication);

        String token = jwtUtil.createToken(afterAuthentication);

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return new CustomResponseBody(SuccessStatusCode.LOGIN_SUCCESS);
    }

}
