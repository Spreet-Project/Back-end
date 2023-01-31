package com.team1.spreet.domain.event.dto;

import com.team1.spreet.domain.event.model.Event;
import com.team1.spreet.domain.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EventDto {

	@NoArgsConstructor
	@Getter
	@Setter
	public static class RequestDto {
		@ApiModelProperty(value = "제목", required = true)
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;

		@ApiModelProperty(value = "행사 게시글 내용", required = true)
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;

		@ApiModelProperty(value = "행사 위치/주소", required = true)
		@NotBlank(message = "주소를 입력해 주세요.")
		private String location;

		@ApiModelProperty(value = "행사 날짜", required = true)
		@NotBlank(message = "날짜를 입력해주세요.")
		private String date;

		@ApiModelProperty(value = "행사 시간", required = true)
		@NotBlank(message = "시간을 입력해주세요.")
		private String time;

		@ApiModelProperty(value = "행사 이미지 파일", required = true)
		@NotNull(message = "이벤트 이미지를 업로드 해주세요.")
		private MultipartFile file;

		public Event toEntity(String eventImageUrl, User user) {
			return new Event(this.title, this.content, this.location, this.date,
				this.time, eventImageUrl, user);
		}
	}

	@NoArgsConstructor
	@Getter
	@Setter
	public static class UpdateRequestDto {
		@ApiModelProperty(value = "제목", required = true)
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;

		@ApiModelProperty(value = "행사 게시글 내용", required = true)
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;

		@ApiModelProperty(value = "행사 위치/주소", required = true)
		@NotBlank(message = "주소를 입력해 주세요.")
		private String location;

		@ApiModelProperty(value = "행사 날짜", required = true)
		@NotBlank(message = "날짜를 입력해주세요.")
		private String date;

		@ApiModelProperty(value = "행사 시간", required = true)
		@NotBlank(message = "시간을 입력해주세요.")
		private String time;

		@ApiModelProperty(value = "행사 이미지 파일")
		@NotNull(message = "이벤트 이미지를 업로드 해주세요.")
		private MultipartFile file;
	}

	@NoArgsConstructor
	@Getter
	public static class ResponseDto implements Serializable {
		@ApiModelProperty(value = "행사 게시글 ID")
		private Long eventId;

		@ApiModelProperty(value = "행사 제목")
		private String title;

		@ApiModelProperty(value = "행사 내용")
		private String content;

		@ApiModelProperty(value = "행사 위치/주소")
		private String location;

		@ApiModelProperty(value = "행사 날짜")
		private String date;

		@ApiModelProperty(value = "행사 시간")
		private String time;

		@ApiModelProperty(value = "행사 이미지")
		private String eventImageUrl;

		@ApiModelProperty(value = "크루 닉네임")
		private String nickname;

		@ApiModelProperty(value = "프로필 이미지")
		private String profileImageUrl;

		public ResponseDto(Event event, String nickname, String profileImageUrl) {
			this.eventId = event.getId();
			this.title = event.getTitle();
			this.content = event.getContent();
			this.location = event.getLocation();
			this.date = event.getDate();
			this.time = event.getTime();
			this.eventImageUrl = event.getEventImageUrl();
			this.nickname = nickname;
			this.profileImageUrl = profileImageUrl;
		}
	}
}
