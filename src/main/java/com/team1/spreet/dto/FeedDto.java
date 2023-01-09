package com.team1.spreet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.entity.Feed;
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
        private String title;
        private String content;
        private List<MultipartFile> file;
    }
    @NoArgsConstructor
    @Getter
    public static class ResponseDto {
        private Long feedId;
        private String nickname;
        private String title;
        private String content;
        private List<String> imageUrlList;
        private Long feedLike;
        private boolean isLike;
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
        private Long feedId;
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
