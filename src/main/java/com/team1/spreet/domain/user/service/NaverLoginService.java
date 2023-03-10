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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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
	@Value("${spring.security.oauth2.client.provider.naver.token-uri}")
	private String tokenUri;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;


	// ????????? ????????? ??????
	public UserDto.LoginResponseDto naverLogin(String code, String state,
		HttpServletResponse response) throws JsonProcessingException {
		//1. ??????????????? state ??? ?????? access_token ????????????
		String accessToken = getToken(code, state);

		//2. access_token ??? ????????? ????????? ?????? ????????????(email, nickname, profile_image)
		UserDto.NaverInfoDto naverInfoDto = getNaverUserInfo(accessToken);

		//3. ?????????????????? ????????? ????????????
		User naverUser = registerNaverUserIfNeeded(naverInfoDto);

		//4. ?????? ?????????
		Authentication authentication = securityLogin(naverUser);

		//5. ??????????????? response
		String token = jwtUtil.createToken(authentication);
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

		return new UserDto.LoginResponseDto(naverUser.getNickname(), naverUser.getUserRole());
	}

	// ?????? ??????
	public String getToken(String code, String state) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("grant_type", "authorization_code");
		body.add("code", code);
		body.add("state", state);

		// POST ?????? ?????????
		HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(body,
			headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(
			tokenUri,
			HttpMethod.POST,
			naverUserInfoRequest,
			String.class
		);

		// response ?????? ?????? ????????????
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}

	// ???????????? ???????????? ????????????
	public UserDto.NaverInfoDto getNaverUserInfo(String token) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTP ?????? ?????????
		HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			userInfoUri,
			HttpMethod.POST,
			naverUserInfoRequest,
			String.class
		);

		// response ?????? ???????????? ????????????
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		String id = jsonNode.get("response").get("id").asText();
		String email = jsonNode.get("response").get("email").asText();
		String nickname = jsonNode.get("response").get("nickname").asText();

		// ??????????????? ????????? ????????????
		String profileImage = jsonNode.get("response").get("profile_image").asText();
		return new UserDto.NaverInfoDto(id, nickname, email, profileImage);
	}

	// ????????? ?????? ????????????
	public User registerNaverUserIfNeeded(UserDto.NaverInfoDto naverInfoDto) {
		String naverId = naverInfoDto.getId();
		User naverUser = userRepository.findByLoginId(naverId).orElse(null);

		// ????????? ???????????? DB ??? ?????? ??????
		if (naverUser == null) {
			// ????????? ???????????? ?????? ??????
			String naverEmail = naverInfoDto.getEmail();
			if (userRepository.findByEmail(naverEmail).isPresent()) {
				throw new RestApiException(ErrorStatusCode.OVERLAPPED_EMAIL);
			}

			// ???????????? ????????? ??????
			String nickname = naverInfoDto.getNickname();
			if (userRepository.findByNickname(nickname).isPresent()) {
				nickname = "naver_" + naverEmail.split("@")[0];
			}

			String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
			String profileImage = naverInfoDto.getProfileImage();

			naverUser = new User(naverId, nickname, encodedPassword, naverEmail,
				profileImage, UserRole.ROLE_USER, Provider.NAVER);

			userRepository.save(naverUser);
		}
		return naverUser;
	}

	// ???????????? ?????? ?????????
	private Authentication securityLogin(User user) {
		if (user.isDeleted()) {
			throw new RestApiException(ErrorStatusCode.NOT_EXIST_USER);
		}
		UserDetails userDetails = new UserDetailsImpl(user);

		return new UsernamePasswordAuthenticationToken(userDetails,
			null, userDetails.getAuthorities());
	}
}
