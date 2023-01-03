package com.team1.spreet.dto;

import com.team1.spreet.entity.Feed;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FeedDto {
    @Getter
    public class RequestDto {
        private String title;
        private String content;
        private List<MultipartFile> multipartFileList;
    }
    public static class ResponseDto {
        private Long feedId;
        private String nickname;
        private String title;
        private String content;
        private List<String> imageList;
        private Long feedLike;
        private boolean isLike;

        public ResponseDto(Feed feed, String nickname, Long feedLike, boolean isLike) {
            this.feedId = feed.getId();
            this.nickname = nickname;
            this.title = feed.getTitle();
            this.content = feed.getContent();
            this.imageList = new ArrayList<>(); //image entity 구현 후 수정
            this.feedLike = feedLike;
            this.isLike = isLike;
        }
    }
}
