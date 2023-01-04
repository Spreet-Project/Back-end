package com.team1.spreet.dto;

import com.team1.spreet.entity.ShortsComment;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsCommentDto {
	@NoArgsConstructor
	@Getter
	public static class RequestDto {
		@NotNull(message = "댓글을 입력해 주세요.")
		private String content;
	}

	@NoArgsConstructor
	@Getter
	public static class ResponseDto {
		private Long shortsCommentId;
		private String nickname;
		private String content;
		private LocalDateTime createdAt;
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
