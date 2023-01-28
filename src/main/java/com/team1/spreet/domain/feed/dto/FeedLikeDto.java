package com.team1.spreet.domain.feed.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FeedLikeDto {

    @NoArgsConstructor
    @Getter
    public static class ResponseDto{

        @ApiModelProperty(value = "좋아요 상태")
        private boolean isLike;

        public ResponseDto(boolean isLike) {
            this.isLike = isLike;
        }
    }
}
