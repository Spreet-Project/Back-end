package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public CustomResponseBody getRecentFeed(Pageable pageable){
        return feedService.getRecentFeed();
    }
    //feed 조회
    @GetMapping("/{feedId}")
    public CustomResponseBody getFeed(@PathVariable Long feedId, UserDetailsImpl userDetailsImpl) {
        return feedService.getFeed(feedId, userDetailsImpl);
    }
    //feed 저장
    @PostMapping("")
    public CustomResponseBody<SuccessStatusCode> saveFeed(@ModelAttribute FeedDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
        return feedService.saveFeed(requestDto, userDetails);
    }
    //feed 수정
    @PutMapping("/{feedId}")
    public CustomResponseBody<SuccessStatusCode> updateFeed(@PathVariable Long feedId, @ModelAttribute FeedDto.RequestDto requestDto, User user){
        return feedService.updateFeed(feedId, requestDto, user);
    }
    //feed 삭제
    @DeleteMapping("/{feedId}")
    public CustomResponseBody<SuccessStatusCode> deleteFeed(@PathVariable Long feedId, User user){
        return feedService.deleteFeed(feedId, user);
    }
    @PostMapping("/like/{feedId}")
    public CustomResponseBody likeFeed(@PathVariable Long feedId, User user){
        return feedService.likeFeed(feedId, user);
    }
}
