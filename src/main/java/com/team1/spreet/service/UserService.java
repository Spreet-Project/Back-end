package com.team1.spreet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


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

//        if(userRepository.existsByLoginIdAndIsDeleted(requestDto.getLoginId(), true))
//            throw new RestApiException(ErrorStatusCode.DELETED_ACCOUNT_EXCEPTION);

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

    //카카오 회원가입 및 로그인 서비스
    public CustomResponseBody kakaoLogin(String code, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        //1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        //2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        UserDto.KakaoInfoDto kakaoInfoDto = getKakaoUserInfo(accessToken);

        //3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoInfoDto);

        //4. JWT 토큰 반환
        String createToken = jwtUtil.createToken(kakaoUser.getLoginId(), kakaoUser.getUserRole());

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return new CustomResponseBody(SuccessStatusCode.LOGIN_SUCCESS);
    }

    private String getToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "a2347db1ceee37de238b04db40b8bb4e");
        body.add("redirect_url", "http://localhost:3000/api/user/kakao/callback");
        body.add("code", code);

        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //Http 응갑 (JSON -> 액세스 토큰 파싱)
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private UserDto.KakaoInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new UserDto.KakaoInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(UserDto.KakaoInfoDto kakaoInfoDto) {
        Long kakaoId = kakaoInfoDto.getId();
        User kakaoUser = userRepository.findByLoginId(kakaoId.toString()).orElse(null);
        if (kakaoUser == null) {
            String kakaoEmail = kakaoInfoDto.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                String email = kakaoInfoDto.getEmail();

                kakaoUser = new User(kakaoId, kakaoInfoDto.getNickname(), encodedPassword, email);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    public CustomResponseBody idCheck(String loginId) {
        if(userRepository.findByLoginId(loginId).isPresent())
            throw new RestApiException(ErrorStatusCode.ID_ALREADY_EXISTS_EXCEPTION);
        return new CustomResponseBody(SuccessStatusCode.ID_DUPLICATE_CHECK);
    }

    public CustomResponseBody nicknameCheck(String nickname) {
        if(userRepository.findByNickname(nickname).isPresent())
            throw new RestApiException(ErrorStatusCode.NICKNAME_ALREADY_EXISTS_EXCEPTION);
        return new CustomResponseBody(SuccessStatusCode.NICKNAME_DUPLICATE_CHECK);
    }
}
