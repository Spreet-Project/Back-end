package com.team1.spreet.dto;

import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
import com.team1.spreet.entity.User;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ShortsDto {

	@NoArgsConstructor
	@Getter
	@Setter
	public static class RequestDto {
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


		public Shorts toEntity(String videoUrl, User user) {
			return new Shorts(this.title, this.content, videoUrl, this.category, user);
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
	public static class ResponseDto {
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
		private String category;

		@ApiModelProperty(value = "좋아요 개수")
		private Long likeCount = 0L;       //좋아요 갯수

		@ApiModelProperty(value = "좋아요 상태")
		private boolean isLike;      //좋아요 상태(true, false)

		public ResponseDto(Shorts shorts, boolean isLike) {
			this.shortsId = shorts.getId();
			this.nickname = shorts.getUser().getNickname();
			this.title = shorts.getTitle();
			this.content = shorts.getContent();
			this.videoUrl = shorts.getVideoUrl();
			this.category = shorts.getCategory().value();
			this.likeCount = shorts.getLikeCount();
			this.isLike = isLike;
		}
	}

	@NoArgsConstructor
	@Getter
	public static class SimpleResponseDto {
		@ApiModelProperty(value = "쇼츠 ID")
		private Long shortsId;

		@ApiModelProperty(value = "제목")
		private String title;

		@ApiModelProperty(value = "영상 url")
		private String videoUrl;

		@ApiModelProperty(value = "카테고리")
		private String category;

		public SimpleResponseDto(Shorts shorts) {
			this.shortsId = shorts.getId();
			this.category = shorts.getCategory().value();
			this.title = shorts.getTitle();
			this.videoUrl = shorts.getVideoUrl();
		}
	}

	@NoArgsConstructor
	@Getter
	public static class CategoryResponseDto {
		@ApiModelProperty(value = "랩 리스트")
		private List<ShortsDto.SimpleResponseDto> rap = new ArrayList<>();

		@ApiModelProperty(value = "디제이 리스트")
		private List<ShortsDto.SimpleResponseDto> dj = new ArrayList<>();

		@ApiModelProperty(value = "비트박스 리스트")
		private List<ShortsDto.SimpleResponseDto> beatBox = new ArrayList<>();

		@ApiModelProperty(value = "스트릿댄스 리스트")
		private List<ShortsDto.SimpleResponseDto> streetDance = new ArrayList<>();

		@ApiModelProperty(value = "그래피티 리스트")
		private List<ShortsDto.SimpleResponseDto> graffiti = new ArrayList<>();

		@ApiModelProperty(value = "기타 리스트")
		private List<ShortsDto.SimpleResponseDto> etc = new ArrayList<>();

		public CategoryResponseDto(List<SimpleResponseDto> rap, List<SimpleResponseDto> dj,
			List<SimpleResponseDto> beatBox, List<SimpleResponseDto> streetDance,
			List<SimpleResponseDto> graffiti, List<SimpleResponseDto> etc) {
			this.rap = rap;
			this.dj = dj;
			this.beatBox = beatBox;
			this.streetDance = streetDance;
			this.graffiti = graffiti;
			this.etc = etc;
		}
	}
}
