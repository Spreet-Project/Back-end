package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedDto;
import com.team1.spreet.dto.FeedLikeDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    //feed 최신순 조회
    @GetMapping("/recent")
    public CustomResponseBody<Page<FeedDto.RecentFeedDto>> getRecentFeed(@RequestParam(value ="page") int page, @RequestParam(value = "size") int size,
                                                                         @AuthenticationPrincipal UserDetails userDetails){
        Long userId = userDetails.getUsername() == null ? 0L : Long.parseLong(userDetails.getUsername());
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getRecentFeed(page, size, userId));
    }
    //feed 조회
    @GetMapping("/{feedId}")
    public CustomResponseBody<FeedDto.ResponseDto> getFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userDetails.getUsername() == null ? 0L : Long.parseLong(userDetails.getUsername());
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getFeed(feedId, userId));
    }
    //feed 저장
    @PostMapping("")
    public CustomResponseBody<SuccessStatusCode> saveFeed(@ModelAttribute FeedDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return new CustomResponseBody<>(feedService.saveFeed(requestDto, userDetails));
    }
    //feed 수정
    @PutMapping("/{feedId}")
    public CustomResponseBody<SuccessStatusCode> updateFeed(@PathVariable Long feedId, @ModelAttribute FeedDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return new CustomResponseBody<>(feedService.updateFeed(feedId, requestDto, userDetails));
    }
    //feed 삭제
    @DeleteMapping("/{feedId}")
    public CustomResponseBody<SuccessStatusCode> deleteFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetails userDetails){
        return new CustomResponseBody<>(feedService.deleteFeed(feedId, userDetails));
    }
    //feed 좋아요
    @PostMapping("/like/{feedId}")
    public CustomResponseBody<FeedLikeDto.ResponseDto> likeFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetails userDetails){
        return feedService.likeFeed(feedId, userDetails);
    }
}
