package com.team1.spreet.domain.shorts.dto;

import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.shorts.model.ShortsComment;
import com.team1.spreet.domain.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsCommentDto {
	@NoArgsConstructor
	@Getter
	public static class RequestDto {
		@NotBlank(message = "댓글을 입력해 주세요.")
		@ApiParam(value = "댓글 내용", required = true)
		private String content;

		public ShortsComment toEntity(String content, Shorts shorts, User user) {
			return new ShortsComment(content, shorts, user);
		}
	}

	@NoArgsConstructor
	@Getter
	public static class ResponseDto {
		@ApiParam(value = "쇼츠 댓글 ID")
		private Long shortsCommentId;

		@ApiParam(value = "닉네임")
		private String nickname;

		@ApiParam(value = "댓글 내용")
		private String content;

		@ApiModelProperty(value = "프로필 이미지")
		private String profileImageUrl;

		@ApiParam(value = "작성 일자")
		private LocalDateTime createdAt;

		@ApiParam(value = "수정 일자")
		private LocalDateTime modifiedAt;

		public ResponseDto(ShortsComment shortsComment) {
			this.shortsCommentId = shortsComment.getId();
			this.nickname = shortsComment.getUser().getNickname();
			this.content = shortsComment.getContent();
			this.profileImageUrl = shortsComment.getUser().getProfileImage();
			this.createdAt = shortsComment.getCreatedAt();
			this.modifiedAt = shortsComment.getModifiedAt();
		}
	}

}
