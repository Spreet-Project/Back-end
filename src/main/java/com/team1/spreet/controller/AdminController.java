package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.UserDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	// 크루 회원 승인 요청 리스트 가져오기
	@ApiOperation(value = "크루 회원 승인 대기 리스트 조회 API")
	@GetMapping("/crew/list")
	public CustomResponseBody<List<UserDto.CrewResponseDto>> getCrewList() {
		return new CustomResponseBody<>(SuccessStatusCode.GET_CREW_LIST, adminService.getCrewList());
	}

	// 크루 회원 승인
	@ApiOperation(value = "크루 회원 승인 API")
	@PutMapping("/approve/{userId}")
	public CustomResponseBody<SuccessStatusCode> approveCrew(@PathVariable @ApiParam(value = "크루 회원 승인을 위한 회원 ID") Long userId) {
		adminService.approveCrew(userId);
		return new CustomResponseBody<>(SuccessStatusCode.APPROVE_ROLE_CREW);
	}

	// 크루 회원 거절
	@ApiOperation(value = "크루 회원 거절 API")
	@PutMapping("/reject/{userId}")
	public CustomResponseBody<SuccessStatusCode> rejectCrew(@PathVariable @ApiParam(value = "크루 회원 거절을 위한 회원 ID") Long userId) {
		adminService.rejectCrew(userId);
		return new CustomResponseBody<>(SuccessStatusCode.REJECT_ROLE_CREW);
	}
}
