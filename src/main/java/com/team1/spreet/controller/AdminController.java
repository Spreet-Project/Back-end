package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto.CrewResponseDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	// 크루 회원 승인 요청 리스트 가져오기
	@ApiOperation(value = "크루 회원 승인 대기 리스트 조회 API")
	@GetMapping("/crew/list")
	public CustomResponseBody<List<CrewResponseDto>> getCrewList() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_CREW_LIST, adminService.getCrewList());
	}

	// 크루 회원 승인
	@ApiOperation(value = "크루 회원 승인 API")
	@PostMapping("/approve/{nickname}")
	public CustomResponseBody<SuccessStatusCode> approveCrew(@PathVariable String nickname) {
		return new CustomResponseBody<>(adminService.approveCrew(nickname));
	}

	// 크루 회원 거절
	@ApiOperation(value = "크루 회원 거절 API")
	@PostMapping("/reject/{nickname}")
	public CustomResponseBody<SuccessStatusCode> rejectCrew(@PathVariable String nickname) {
		return new CustomResponseBody<>(adminService.rejectCrew(nickname));
	}
}