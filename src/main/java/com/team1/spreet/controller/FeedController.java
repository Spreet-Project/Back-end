package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedDto;
import com.team1.spreet.dto.FeedLikeDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.FeedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "feed")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    //feed 최신순 조회
    @ApiOperation(value = "피드 최신순 조회 API")
    @GetMapping("/recent")
    public CustomResponseBody<Page<FeedDto.RecentFeedDto>> getRecentFeed(@RequestParam(value ="page") int page, @RequestParam(value = "size") int size,
                                                                         @AuthenticationPrincipal UserDetails userDetails){
        Long userId = userDetails.getUsername() == null ? 0L : Long.parseLong(userDetails.getUsername());
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getRecentFeed(page, size, userId));
    }
    //feed 조회
    @ApiOperation(value = "피드 상세조회 API")
    @GetMapping("/{feedId}")
    public CustomResponseBody<FeedDto.ResponseDto> getFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userDetails.getUsername() == null ? 0L : Long.parseLong(userDetails.getUsername());
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getFeed(feedId, userId));
    }
    //feed 저장
    @ApiOperation(value = "피드 등록 API")
    @PostMapping("")
    public CustomResponseBody<SuccessStatusCode> saveFeed(@ModelAttribute FeedDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return new CustomResponseBody<>(feedService.saveFeed(requestDto, userDetails));
    }
    //feed 수정
    @ApiOperation(value = "피드 수정 API")
    @PutMapping("/{feedId}")
    public CustomResponseBody<SuccessStatusCode> updateFeed(@PathVariable Long feedId, @ModelAttribute FeedDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return new CustomResponseBody<>(feedService.updateFeed(feedId, requestDto, userDetails));
    }
    //feed 삭제
    @DeleteMapping("/{feedId}")
    @ApiOperation(value = "피드 삭제 API")
    public CustomResponseBody<SuccessStatusCode> deleteFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetails userDetails){
        return new CustomResponseBody<>(feedService.deleteFeed(feedId, userDetails));
    }
    //feed 좋아요
    @PostMapping("/like/{feedId}")
    @ApiOperation(value = "피드 좋아요 등록/취소 API")
    public CustomResponseBody<FeedLikeDto.ResponseDto> likeFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetails userDetails){
        return feedService.likeFeed(feedId, userDetails);
    }
}
