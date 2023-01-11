package com.team1.spreet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
import io.swagger.annotations.ApiParam;
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
		@ApiParam(value = "제목", required = true)
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;

		@ApiParam(value = "게시글 내용", required = true)
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;

		@ApiParam(value = "영상 파일", required = true)
		@NotNull(message = "영상을 업로드해 주세요.")
		private MultipartFile file;

		@ApiParam(value = "카테고리", required = true)
		@NotNull(message = "카테고리를 선택해 주세요.")
		private Category category;
	}

	@NoArgsConstructor
	@Getter
	@Setter
	public static class UpdateRequestDto {
		@ApiParam(value = "제목", required = true)
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;

		@ApiParam(value = "게시글 내용", required = true)
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;

		@ApiParam(value = "영상 파일", required = false)
		private MultipartFile file;               // shorts 수정시, 영상 수정 없이 글만 수정 가능

		@ApiParam(value = "카테고리", required = true)
		@NotNull(message = "카테고리를 선택해 주세요.")
		private Category category;
	}

	@NoArgsConstructor
	@Getter
	public static class ResponseDto {
		@ApiParam(value = "쇼츠 ID")
		private Long shortsId;

		@ApiParam(value = "닉네임")
		private String nickname;

		@ApiParam(value = "제목")
		private String title;

		@ApiParam(value = "내용")
		private String content;

		@ApiParam(value = "영상 url")
		private String videoUrl;

		@ApiParam(value = "카테고리")
		private String category;

		@ApiParam(value = "좋아요 개수", defaultValue = "0")
		private Long likeCount = 0L;       //좋아요 갯수

		@ApiParam(value = "좋아요 상태")
		private boolean isLike;      //좋아요 상태(true, false)

		@ApiParam(value = "쇼츠 댓글 리스트")
		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		private List<ShortsCommentDto.ResponseDto> shortsCommentList = new ArrayList<>();

		public ResponseDto(Shorts shorts, boolean isLike, List<ShortsCommentDto.ResponseDto> shortsCommentList) {
			this.shortsId = shorts.getId();
			this.nickname = shorts.getUser().getNickname();
			this.title = shorts.getTitle();
			this.content = shorts.getContent();
			this.videoUrl = shorts.getVideoUrl();
			this.category = shorts.getCategory().value();
			this.likeCount = shorts.getLikeCount();
			this.isLike = isLike;
			this.shortsCommentList = shortsCommentList;
		}
	}

	@NoArgsConstructor
	@Getter
	public static class SimpleResponseDto {
		@ApiParam(value = "쇼츠 ID")
		private Long shortsId;

		@ApiParam(value = "제목")
		private String title;

		@ApiParam(value = "영상 url")
		private String videoUrl;

		@ApiParam(value = "카테고리")
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
		@ApiParam(value = "랩 리스트")
		private List<ShortsDto.SimpleResponseDto> rap = new ArrayList<>();

		@ApiParam(value = "디제이 리스트")
		private List<ShortsDto.SimpleResponseDto> dj = new ArrayList<>();

		@ApiParam(value = "비트박스 리스트")
		private List<ShortsDto.SimpleResponseDto> beatBox = new ArrayList<>();

		@ApiParam(value = "스트릿댄스 리스트")
		private List<ShortsDto.SimpleResponseDto> streetDance = new ArrayList<>();

		@ApiParam(value = "그래피티 리스트")
		private List<ShortsDto.SimpleResponseDto> graffiti = new ArrayList<>();

		@ApiParam(value = "기타 리스트")
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
