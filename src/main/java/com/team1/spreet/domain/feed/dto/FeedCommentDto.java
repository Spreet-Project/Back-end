package com.team1.spreet.domain.feed.dto;

import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.model.FeedComment;
import com.team1.spreet.domain.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class FeedCommentDto {
    @Getter
    public static class RequestDto{
        @ApiModelProperty(value = "피드 댓글 내용", required = true)
        @NotBlank(message = "댓글을 입력해 주세요.")
        private String content;

        public FeedComment toEntity(Feed feed, User user){
            return new FeedComment(this.content, feed, user);
        }
    }

    @NoArgsConstructor
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
}
