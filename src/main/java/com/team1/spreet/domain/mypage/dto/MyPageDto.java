package com.team1.spreet.domain.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.domain.shorts.model.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

public class MyPageDto {

	@NoArgsConstructor
	@Getter
	public static class NicknameRequestDto {
		@ApiModelProperty(value = "닉네임", required = true)
		@NotBlank(message = "닉네임을 입력해주세요")
		private String nickname;
	}

	@NoArgsConstructor
	@Getter
	public static class PasswordRequestDto {
		@ApiModelProperty(value = "비밀번호")
		@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
		private String password;
	}

	@NoArgsConstructor
	@Getter
	public static class UserInfoResponseDto {
		@ApiModelProperty(value = "로그인 ID")
		private String loginId;

		@ApiModelProperty(value = "닉네임")
		private String nickname;

		@ApiModelProperty(value = "이메일")
		private String email;

		@ApiModelProperty(value = "프로필 이미지")
		private String profileImage;

		public UserInfoResponseDto(String loginId, String nickname, String email, String profileImage) {
			this.loginId = loginId;
			this.nickname = nickname;
			this.email = email;
			this.profileImage = profileImage;
		}
	}

	// 회원이 작성한 게시글을 반환하기 위한 Dto
	@NoArgsConstructor
	@Getter
	public static class PostResponseDto implements Serializable {
		@ApiModelProperty(value = "게시글 분류")
		private String classification;

		@ApiModelProperty(value = "게시글 ID")
		private Long id;

		@ApiModelProperty(value = "제목")
		private String title;

		@ApiModelProperty(value = "카테고리")
		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		private Category category;

		@ApiModelProperty(value = "등록 일자")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm")
		private LocalDateTime createdAt;

		public PostResponseDto(String classification, Long id, String title, Category category, LocalDateTime createdAt) {
			this.classification = classification;
			this.id = id;
			this.title = title;
			this.category = category;
			this.createdAt = createdAt;
		}

		public PostResponseDto(String classification, Long id, String title, LocalDateTime createdAt) {
			this.classification = classification;
			this.id = id;
			this.title = title;
			this.createdAt = createdAt;
		}
	}
}
