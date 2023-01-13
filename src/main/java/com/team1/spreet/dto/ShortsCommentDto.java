package com.team1.spreet.dto;

import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.ShortsComment;
import com.team1.spreet.entity.User;
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

		public ShortsComment toEntity(Shorts shorts, User user) {
			return new ShortsComment(this.content, shorts, user);
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

		@ApiParam(value = "작성 일자")
		private LocalDateTime createdAt;

		@ApiParam(value = "수정 일자")
		private LocalDateTime modifiedAt;

		public ResponseDto(ShortsComment shortsComment) {
			this.shortsCommentId = shortsComment.getId();
			this.nickname = shortsComment.getUser().getNickname();
			this.content = shortsComment.getContent();
			this.createdAt = shortsComment.getCreatedAt();
			this.modifiedAt = shortsComment.getModifiedAt();
		}
	}

}
