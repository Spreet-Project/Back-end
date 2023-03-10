package com.team1.spreet.domain.shorts.dto;

import com.team1.spreet.domain.shorts.model.Category;
import com.team1.spreet.domain.shorts.model.Shorts;
import com.team1.spreet.domain.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ShortsDto {

	@NoArgsConstructor
	@Getter
	@Setter
	public static class RequestDto implements Serializable {
		@ApiModelProperty(value = "제목", required = true)
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;

		@ApiModelProperty(value = "게시글 내용", required = true)
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;

		@ApiModelProperty(value = "영상 파일", required = true)
		@NotNull(message = "영상을 업로드해 주세요.")
		private MultipartFile file;

		@ApiModelProperty(value = "카테고리", required = true)
		@NotNull(message = "카테고리를 선택해 주세요.")
		private Category category;


		public Shorts toEntity(String title, String content, String videoUrl, User user) {
			return new Shorts(title, content, videoUrl, this.category, user);
		}
	}

	@NoArgsConstructor
	@Getter
	@Setter
	public static class UpdateRequestDto {
		@ApiModelProperty(value = "제목", required = true)
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;

		@ApiModelProperty(value = "게시글 내용", required = true)
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;

		@ApiModelProperty(value = "영상 파일")
		private MultipartFile file;               // shorts 수정시, 영상 수정 없이 글만 수정 가능

		@ApiModelProperty(value = "카테고리", required = true)
		@NotNull(message = "카테고리를 선택해 주세요.")
		private Category category;
	}

	@NoArgsConstructor
	@Getter
	public static class MainResponseDto implements Serializable {
		@ApiModelProperty(value = "쇼츠 ID")
		private Long shortsId;

		@ApiModelProperty(value = "닉네임")
		private String nickname;

		@ApiModelProperty(value = "제목")
		private String title;

		@ApiModelProperty(value = "영상 url")
		private String videoUrl;

		@ApiModelProperty(value = "카테고리")
		private Category category;

		@ApiModelProperty(value = "프로필 이미지")
		private String profileImageUrl;
	}

	@NoArgsConstructor
	@Getter
	public static class ResponseDto implements Serializable {
		@ApiModelProperty(value = "쇼츠 ID")
		private Long shortsId;

		@ApiModelProperty(value = "닉네임")
		private String nickname;

		@ApiModelProperty(value = "제목")
		private String title;

		@ApiModelProperty(value = "내용")
		private String content;

		@ApiModelProperty(value = "영상 url")
		private String videoUrl;

		@ApiModelProperty(value = "카테고리")
		private Category category;

		@ApiModelProperty(value = "프로필 이미지")
		private String profileImageUrl;

		@ApiModelProperty(value = "좋아요 개수")
		private Long likeCount = 0L;       //좋아요 갯수

		@ApiModelProperty(value = "좋아요 상태")
		private boolean liked;      //좋아요 상태(true, false)

		@ApiModelProperty(value = "구독 유무")
		private boolean subscribed;      //true, false

		public ResponseDto(Shorts shorts, boolean liked, boolean subscribed) {
			this.shortsId = shorts.getId();
			this.nickname = shorts.getUser().getNickname();
			this.title = shorts.getTitle();
			this.content = shorts.getContent();
			this.videoUrl = shorts.getVideoUrl();
			this.category = shorts.getCategory();
			this.profileImageUrl = shorts.getUser().getProfileImage();
			this.likeCount = shorts.getLikeCount();
			this.liked = liked;
			this.subscribed = subscribed;
		}
	}

}
