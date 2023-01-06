package com.team1.spreet.dto;

import com.team1.spreet.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FeedDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class RequestDto {
        private String title;
        private String content;
        private List<MultipartFile> file;
    }
    public static class ResponseDto {
        private Long feedId;
        private String nickname;
        private String title;
        private String content;
        private List<String> imageList;
        private Long feedLike;
        private boolean isLike;
        private List<FeedCommentDto.ResponseDto> commentList;

        public ResponseDto(Feed feed, String nickname, Long feedLike, boolean isLike, FeedCommentDto.CommentListDto commentListDto) {
            this.feedId = feed.getId();
            this.nickname = nickname;
            this.title = feed.getTitle();
            this.content = feed.getContent();
            this.imageList = new ArrayList<>(); //image entity 구현 후 수정
            this.feedLike = feedLike;
            this.isLike = isLike;
            this.commentList = commentListDto.getCommentList();
        }
    }
    public class RecentFeedDto{
        private Long feedId;
        private String title;
        public RecentFeedDto(Long feedId, String title) {
            this.feedId = feedId;
            this.title = title;
        }
    }
}
