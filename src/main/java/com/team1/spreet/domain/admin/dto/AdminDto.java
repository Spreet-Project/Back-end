package com.team1.spreet.domain.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminDto {

	@NoArgsConstructor
	@Getter
	public static class CrewResponseDto {
		@ApiModelProperty(value = "회원 고유 ID")
		private Long userId;

		@ApiModelProperty(value = "로그인 ID")
		private String loginId;

		@ApiModelProperty(value = "닉네임")
		private String nickname;

		public CrewResponseDto(Long userId, String loginId, String nickname) {
			this.userId = userId;
			this.loginId = loginId;
			this.nickname = nickname;
		}
	}
}
