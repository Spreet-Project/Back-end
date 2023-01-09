package com.team1.spreet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
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
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;
		@NotNull(message = "영상을 업로드해 주세요.")
		private MultipartFile file;
		@NotNull(message = "카테고리를 선택해 주세요.")
		private Category category;
	}

	@NoArgsConstructor
	@Getter
	@Setter
	public static class UpdateRequestDto {
		@NotBlank(message = "제목을 입력해 주세요.")
		private String title;
		@NotBlank(message = "내용을 입력해 주세요.")
		private String content;
		private MultipartFile file;               // shorts 수정시, 영상 수정 없이 글만 수정 가능
		@NotNull(message = "카테고리를 선택해 주세요.")
		private Category category;
	}

	@NoArgsConstructor
	@Getter
	public static class ResponseDto {
		private Long shortsId;
		private String nickname;
		private String title;
		private String content;
		private String videoUrl;
		private String category;
		private Long likeCount = 0L;       //좋아요 갯수
		private boolean isLike;      //좋아요 상태(true, false)
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
		private Long shortsId;
		private String title;
		private String videoUrl;
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
		private List<ShortsDto.SimpleResponseDto> rapList = new ArrayList<>();
		private List<ShortsDto.SimpleResponseDto> djList = new ArrayList<>();
		private List<ShortsDto.SimpleResponseDto> beatBoxList = new ArrayList<>();
		private List<ShortsDto.SimpleResponseDto> streetDancList = new ArrayList<>();
		private List<ShortsDto.SimpleResponseDto> gravityList = new ArrayList<>();
		private List<ShortsDto.SimpleResponseDto> etcList = new ArrayList<>();

		public CategoryResponseDto(List<SimpleResponseDto> rapList, List<SimpleResponseDto> djList,
			List<SimpleResponseDto> beatBoxList, List<SimpleResponseDto> streetDancList,
			List<SimpleResponseDto> gravityList, List<SimpleResponseDto> etcList) {

			this.rapList = rapList;
			this.djList = djList;
			this.beatBoxList = beatBoxList;
			this.streetDancList = streetDancList;
			this.gravityList = gravityList;
			this.etcList = etcList;
		}
	}
}
