package com.team1.spreet.dto;

import com.team1.spreet.entity.FeedComment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedCommentDto {
    @Getter
    public static class RequestDto{
        private String content;
    }

    @Getter
    public static class ResponseDto{
        private Long commentId;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public ResponseDto(FeedComment feedComment, String nickname) {
            this.commentId = feedComment.getId();
            this.nickname = nickname;
            this.content = feedComment.getContent();
            this.createdAt = feedComment.getCreatedAt();
            this.modifiedAt = feedComment.getModifiedAt();
        }
    }
    @Getter
    public static class CommentListDto{
        private List<ResponseDto> commentList = new ArrayList<>();

        public void addComment(ResponseDto responseDto) {
            commentList.add(responseDto);
        }
    }
}
