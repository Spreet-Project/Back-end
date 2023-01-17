package com.team1.spreet.dto;

import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class FeedDto {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestDto {
        @ApiModelProperty(value = "제목", required = true)
        private String title;

        @ApiModelProperty(value = "게시글 내용", required = true)
        private String content;

        @ApiModelProperty(value = "이미지 파일", required = false)
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

        @ApiModelProperty(value = "좋아요 개수")
        private Long feedLike;

        @ApiModelProperty(value = "좋아요 상태")
        private boolean isLike;

        public ResponseDto(Feed feed, List<String> imageUrlList, Long feedLike, boolean isLike) {
            this.feedId = feed.getId();
            this.nickname = feed.getUser().getNickname();
            this.title = feed.getTitle();
            this.content = feed.getContent();
            this.imageUrlList = imageUrlList;
            this.feedLike = feedLike;
            this.isLike = isLike;
        }
    }
}
