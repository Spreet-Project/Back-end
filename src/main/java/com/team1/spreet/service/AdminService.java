package com.team1.spreet.service;

import com.team1.spreet.dto.UserDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

	private final UserRepository userRepository;

	// 크루회원 승인 대기 리스트 조회
	@Transactional(readOnly = true)
	public List<UserDto.CrewResponseDto> getCrewList() {
		List<User> userList = userRepository.findByUserRoleAndIsCrew(UserRole.ROLE_CREW, false);

		List<UserDto.CrewResponseDto> crewList = new ArrayList<>();
		for (User user : userList) {
			crewList.add(new UserDto.CrewResponseDto(user));
		}

		return crewList;
	}

	// 크루 회원 승인
	public SuccessStatusCode approveCrew(String nickname) {
		User user = userRepository.findByNickname(nickname).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_USER));

		user.approveCrew();

		return SuccessStatusCode.APPROVE_ROLE_CREW;
	}

	// 크루 회원 거절
	public SuccessStatusCode rejectCrew(String nickname) {
		User user = userRepository.findByNickname(nickname).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_FOUND_USER));

		user.rejectCrew();

		return SuccessStatusCode.REJECT_ROLE_CREW;
	}
}
