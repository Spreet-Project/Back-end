package com.team1.spreet.domain.feed.controller;

import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.domain.feed.dto.FeedLikeDto;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.domain.feed.service.FeedLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "feedLike")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedLikeController {
    private final FeedLikeService feedLikeService;
    //feed 좋아요
    @PostMapping("/like/{feedId}")
    @ApiOperation(value = "피드 좋아요 등록/취소 API")
    public CustomResponseBody<FeedLikeDto.ResponseDto> likeFeed(@PathVariable @ApiParam(value = "좋아요 등록/취소 할 피드 ID") Long feedId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return feedLikeService.setFeedLike(feedId, userDetails.getUser());
    }
}
