package com.team1.spreet.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.domain.user.dto.UserDto;
import com.team1.spreet.domain.user.model.Provider;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.auth.jwt.JwtUtil;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginService {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	//카카오 회원가입 및 로그인 서비스
	public UserDto.LoginResponseDto kakaoLogin(String code, HttpServletResponse httpServletResponse)
		throws JsonProcessingException {
		//1. "인가 코드"로 "액세스 토큰" 요청
		String accessToken = getToken(code);

		//2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
		UserDto.KakaoInfoDto kakaoInfoDto = getKakaoUserInfo(accessToken);

		//3. 필요시에 회원가입
		User kakaoUser = registerKakaoUserIfNeeded(kakaoInfoDto);

		UserDetails kakaoUserDetails = new UserDetailsImpl(kakaoUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoUserDetails,
			null, kakaoUserDetails.getAuthorities());

		String createToken = jwtUtil.createToken(authentication);

		httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

		return new UserDto.LoginResponseDto(kakaoUser.getNickname(), kakaoUser.getUserRole());
	}

	private String getToken(String code) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		//HTTP Body 생성
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "a2347db1ceee37de238b04db40b8bb4e");
		body.add("redirect_url", "https://www.spreet.co.kr/api/user/kakao/callback");
		body.add("code", code);

		//HTTP 요청 보내기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body,
			headers);

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

	private UserDto.KakaoInfoDto getKakaoUserInfo(String accessToken)
		throws JsonProcessingException {
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
		String email = jsonNode.get("kakao_account").get("email").asText();
		String nickname = jsonNode.get("properties").get("nickname").asText();
		String profileImage = jsonNode.get("properties").get("profile_image").asText();

		return new UserDto.KakaoInfoDto(id, nickname, email, profileImage);
	}

	private User registerKakaoUserIfNeeded(UserDto.KakaoInfoDto kakaoInfoDto) {
		String kakaoId = kakaoInfoDto.getId().toString();
		User kakaoUser = userRepository.findByLoginId(kakaoId).orElse(null);

		// 카카오 아이디가 DB 에 없는 경우
		if (kakaoUser == null) {
			// 중복된 이메일이 있는 경우
			String kakaoEmail = kakaoInfoDto.getEmail();
			if (userRepository.findByEmail(kakaoEmail).isPresent()) {
				throw new RestApiException(ErrorStatusCode.OVERLAPPED_EMAIL);
			}

			// 닉네임이 중복된 경우
			String nickname = kakaoInfoDto.getNickname();
			if (userRepository.findByNickname(nickname).isPresent()) {
				nickname = "kakao_" + kakaoEmail.split("@")[0];
			}

			String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
			String profileImage = kakaoInfoDto.getProfileImage();

			kakaoUser = new User(kakaoId, nickname, encodedPassword, kakaoEmail,
				profileImage, UserRole.ROLE_USER, Provider.KAKAO);

			userRepository.save(kakaoUser);
		}
		return kakaoUser;
	}

}
