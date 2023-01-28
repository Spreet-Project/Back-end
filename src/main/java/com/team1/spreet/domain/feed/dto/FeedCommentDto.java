package com.team1.spreet.domain.feed.dto;

import com.team1.spreet.domain.feed.model.FeedComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedCommentDto {
    @Getter
    public static class RequestDto{
        @ApiModelProperty(value = "피드 댓글 내용", required = true)
        private String content;
    }

    @Getter
    public static class ResponseDto{
        @ApiModelProperty(value = "피드 댓글 ID")
        private Long commentId;

        @ApiModelProperty(value = "닉네임")
        private String nickname;

        @ApiModelProperty(value = "피드 댓글 내용")
        private String content;

        @ApiModelProperty(value = "프로필 이미지")
        private String profileImageUrl;

        @ApiModelProperty(value = "작성 일자")
        private LocalDateTime createdAt;

        @ApiModelProperty(value = "수정 일자")
        private LocalDateTime modifiedAt;

        public ResponseDto(FeedComment feedComment) {
            this.commentId = feedComment.getId();
            this.nickname = feedComment.getUser().getNickname();
            this.content = feedComment.getContent();
            this.profileImageUrl = feedComment.getUser().getProfileImage();
            this.createdAt = feedComment.getCreatedAt();
            this.modifiedAt = feedComment.getModifiedAt();
        }
    }
    @Getter
    public static class CommentListDto{
        @ApiModelProperty(value = "피드 댓글 리스트")
        private List<ResponseDto> commentList = new ArrayList<>();

        public void addComment(ResponseDto responseDto) {
            commentList.add(responseDto);
        }
    }
}
