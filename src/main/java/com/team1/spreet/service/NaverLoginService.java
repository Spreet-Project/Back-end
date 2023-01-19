package com.team1.spreet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.jwt.JwtUtil;
import com.team1.spreet.repository.UserRepository;
import com.team1.spreet.security.UserDetailsImpl;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class NaverLoginService {
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String clientSecret;
	@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
	private String userInfoUri;
	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
	private String redirectUri;
	@Value("${spring.security.oauth2.client.provider.naver.token-uri}")
	private String tokenUri;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;


	// 네이버 로그인 로직
	public UserDto.LoginResponseDto naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
		//1. 인가코드와 state 를 통해 access_token 발급받기
		String accessToken = getToken(code, state);

		//2. access_token 을 이용해 사용자 정보 가져오기(email, nickname, profile_image)
		UserDto.NaverInfoDto naverInfoDto = getNaverUserInfo(accessToken);

		//3. 사용자정보를 토대로 가입진행
		User naverUser = registerNaverUserIfNeeded(naverInfoDto);

		//4. 강제 로그인
		securityLogin(naverUser);

		//5. 토큰발급후 response
		String token = jwtUtil.createToken(naverUser.getId(), naverUser.getUserRole());
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

		return new UserDto.LoginResponseDto(naverUser.getNickname());
	}

	// 토큰 발급
	public String getToken(String code, String state) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("grant_type", "authorization_code");
		body.add("redirect_uri", redirectUri);
		body.add("code", code);
		body.add("state", state);

		// POST 요청 보내기
		HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(body, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(
			tokenUri,
			HttpMethod.POST,
			naverUserInfoRequest,
			String.class
		);

		// response 에서 토큰 가져오기
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}

	// 토큰으로 유저정보 가져오기
	public UserDto.NaverInfoDto getNaverUserInfo(String token) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTP 요청 보내기
		HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			userInfoUri,
			HttpMethod.POST,
			naverUserInfoRequest,
			String.class
		);

		// response 에서 유저정보 가져오기
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		String id = jsonNode.get("response").get("id").asText();
		String email = jsonNode.get("response").get("email").asText();
		String nickname = jsonNode.get("response").get("nickname").asText();

		// 네이버에서 이미지 가져오기
		String profileImage = jsonNode.get("response").get("profile_image").asText();
		String naverDefaultImage = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";
		String defaultImage = "https://spreet-bucket.s3.ap-northeast-2.amazonaws.com/spreet+%E1%84%85%E1%85%A9%E1%84%80%E1%85%A92.png";
		if (profileImage == null || profileImage.equals(naverDefaultImage)) {
			profileImage = defaultImage;
		}
		return new UserDto.NaverInfoDto(id, nickname, email, profileImage);
	}

	// 필요한 경우 회원가입
	public User registerNaverUserIfNeeded(UserDto.NaverInfoDto naverInfoDto) {
		String naverId = naverInfoDto.getId();
		User naverUser = userRepository.findByLoginId(naverId).orElse(null);

		if (naverUser == null) {
			String naverEmail = naverInfoDto.getEmail();
			User sameEmailUser = userRepository.findByEmail(naverEmail).orElse(null);

			if (sameEmailUser != null) {
				naverUser = sameEmailUser;
				naverUser = naverUser.socialIdUpdate(naverId);
			} else {
				String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
				String email = naverInfoDto.getEmail();
				String profileImage = naverInfoDto.getProfileImage();

				naverUser = new User(naverId, naverInfoDto.getNickname(), encodedPassword, email, profileImage);
			}
			userRepository.save(naverUser);
		}
		return naverUser;
	}

	// 시큐리티 강제 로그인
	private void securityLogin(User user) {
		UserDetails userDetails = new UserDetailsImpl(user);
		if (user.isDeleted()) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
			null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
