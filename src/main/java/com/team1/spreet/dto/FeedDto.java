package com.team1.spreet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.entity.Feed;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FeedDto {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestDto {
        @ApiParam(value = "제목", required = true)
        private String title;

        @ApiParam(value = "게시글 내용", required = true)
        private String content;

        @ApiParam(value = "이미지 파일", required = false)
        private List<MultipartFile> file;

    }

    @NoArgsConstructor
    @Getter
    public static class ResponseDto {
        @ApiParam(value = "피드 ID")
        private Long feedId;

        @ApiParam(value = "닉네임")
        private String nickname;

        @ApiParam(value = "제목")
        private String title;

        @ApiParam(value = "게시글 내용")
        private String content;

        @ApiParam(value = "이미지 url 리스트")
        private List<String> imageUrlList;

        @ApiParam(value = "좋아요 개수")
        private Long feedLike;

        @ApiParam(value = "좋아요 상태")
        private boolean isLike;

        @ApiParam(value = "피드 댓글 리스트")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<FeedCommentDto.ResponseDto> commentList;

        public ResponseDto(Feed feed, List<String> imageUrlList, Long feedLike, boolean isLike, List<FeedCommentDto.ResponseDto> commentList) {
            this.feedId = feed.getId();
            this.nickname = feed.getUser().getNickname();
            this.title = feed.getTitle();
            this.content = feed.getContent();
            this.imageUrlList = imageUrlList;
            this.feedLike = feedLike;
            this.isLike = isLike;
            this.commentList = commentList;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class RecentFeedDto{
        @ApiParam(value = "피드 ID")
        private Long feedId;

        @ApiParam(value = "제목")
        private String title;

        public RecentFeedDto(Feed feed) {
            this.feedId = feed.getId();
            this.title = feed.getTitle();
        }
        public static RecentFeedDto entityToDto(Feed feed) {
            return new RecentFeedDto(feed);
        }
    }
}
