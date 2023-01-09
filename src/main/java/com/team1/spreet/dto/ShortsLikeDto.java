package com.team1.spreet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsLikeDto {

	@NoArgsConstructor
	@Getter
	public static class ResponseDto {
		private boolean isLike;

		public ResponseDto(boolean isLike) {
			this.isLike = isLike;
		}
	}
}
