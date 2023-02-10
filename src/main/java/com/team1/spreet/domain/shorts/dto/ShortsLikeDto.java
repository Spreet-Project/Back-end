package com.team1.spreet.domain.shorts.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsLikeDto {

	@NoArgsConstructor
	@Getter
	public static class ResponseDto {
		@ApiModelProperty(value = "좋아요 상태")
		private boolean liked;

		public ResponseDto(boolean liked) {
			this.liked = liked;
		}
	}
}
