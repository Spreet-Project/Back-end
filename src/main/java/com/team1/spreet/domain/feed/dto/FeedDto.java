package com.team1.spreet.domain.feed.dto;

import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class FeedDto {
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

        @ApiModelProperty(value = "이미지 파일")
        private List<MultipartFile> file;

        public Feed toEntity(User user){
            return new Feed(this.title, this.content, user);
        }
    }

    @NoArgsConstructor
    @Getter
    public static class ResponseDto {
        @ApiModelProperty(value = "피드 ID")
        private Long feedId;

        @ApiModelProperty(value = "닉네임")
        private String nickname;

        @ApiModelProperty(value = "제목")
        private String title;

        @ApiModelProperty(value = "게시글 내용")
        private String content;

        @ApiModelProperty(value = "이미지 url 리스트")
        private List<String> imageUrlList;

        @ApiModelProperty(value = "프로필 이미지")
        private String profileImageUrl;

        @ApiModelProperty(value = "좋아요 개수")
        private Long feedLike;

        @ApiModelProperty(value = "좋아요 상태")
        private boolean liked;

        public ResponseDto(Feed feed, List<String> imageUrlList, Long feedLike, boolean liked) {
            this.feedId = feed.getId();
            this.nickname = feed.getUser().getNickname();
            this.title = feed.getTitle();
            this.content = feed.getContent();
            this.imageUrlList = imageUrlList;
            this.profileImageUrl = feed.getUser().getProfileImage();
            this.feedLike = feedLike;
            this.liked = liked;
        }
    }
    @NoArgsConstructor
    @Getter
    public static class SimpleResponseDto{

        @ApiModelProperty(value = "피드 ID")
        private Long feedId;

        @ApiModelProperty(value = "제목")
        private String title;

        public SimpleResponseDto(Feed feed){
            this.feedId = feed.getId();
            this.title = feed.getTitle();
        }
    }
}
