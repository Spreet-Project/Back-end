package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedCommentDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.service.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;
    //댓글 생성
    @PostMapping("/{feedId}/comment")
    public CustomResponseBody<FeedCommentDto.ResponseDto> saveFeedComment(@PathVariable Long feedId, @RequestBody FeedCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_FEED_COMMENT,feedCommentService.saveFeedComment(feedId, requestDto, userDetails));
    }
    //댓글 수정
    @PutMapping("/comment/{commentId}")
    public CustomResponseBody<FeedCommentDto.ResponseDto> updateFeedComment(@PathVariable Long commentId, @RequestBody FeedCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_FEED_COMMENT, feedCommentService.updateFeedComment(commentId, requestDto, userDetails));
    }
    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    private CustomResponseBody<SuccessStatusCode> deleteFeedComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        return new CustomResponseBody<>(feedCommentService.deleteFeedComment(commentId, userDetails));
    }
}
