package com.team1.spreet.dto;

import com.team1.spreet.entity.Event;
import com.team1.spreet.entity.EventComment;
import com.team1.spreet.entity.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class EventCommentDto {
    @NoArgsConstructor
    @Getter
    public static class RequestDto{
        @NotBlank(message = "댓글을 입력해 주세요")
        @ApiParam(value = "댓글 내용", required = true)
        private String content;

        public EventComment toEntity(String content, Event event, User user){
            return new EventComment(content, event, user);
        }
    }
    @NoArgsConstructor
    @Getter
    public static class ResponseDto{
        @ApiParam(value = "행사 댓글 ID")
        private Long eventCommentId;

        @ApiParam(value = "닉네임")
        private String nickname;

        @ApiParam(value = "행사 댓글 내용")
        private String content;

        @ApiModelProperty(value = "작성 일자")
        private LocalDateTime createdAt;

        @ApiModelProperty(value = "수정 일자")
        private LocalDateTime modifiedAt;

        public ResponseDto(EventComment eventComment) {
            this.eventCommentId = eventComment.getId();
            this.nickname = eventComment.getUser().getNickname();
            this.content = eventComment.getContent();
            this.createdAt = eventComment.getCreatedAt();
            this.modifiedAt = eventComment.getModifiedAt();
        }
    }
}
