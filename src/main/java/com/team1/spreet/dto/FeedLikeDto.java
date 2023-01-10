package com.team1.spreet.dto;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FeedLikeDto {

    @NoArgsConstructor
    @Getter
    public static class ResponseDto{

        @ApiParam(value = "좋아요 상태")
        private boolean isLike;

        public ResponseDto(boolean isLike) {
            this.isLike = isLike;
        }
    }
}
