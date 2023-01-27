package com.team1.spreet.service;

import com.team1.spreet.dto.UserDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
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
		List<User> userList = userRepository.findByUserRoleAndIsDeletedFalse(UserRole.ROLE_UNACCEPTED_CREW);

		List<UserDto.CrewResponseDto> crewList = new ArrayList<>();
		for (User user : userList) {
			crewList.add(new UserDto.CrewResponseDto(user.getId(), user.getLoginId(), user.getNickname()));
		}

		return crewList;
	}

	// 크루 회원 승인
	public void approveCrew(Long userId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER));

		user.approveCrew();
	}

	// 크루 회원 거절
	public void rejectCrew(Long userId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(
			() -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER));

		user.rejectCrew();
	}
}
