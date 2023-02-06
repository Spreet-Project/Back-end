package com.team1.spreet.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class BadWordDto {

	@NoArgsConstructor
	@Getter
	public static class RequestDto {
		private String content;
	}
}
