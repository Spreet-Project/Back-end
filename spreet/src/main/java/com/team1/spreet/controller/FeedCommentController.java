package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedCommentDto;
import com.team1.spreet.entity.User;
import com.team1.spreet.service.FeedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;

    @PostMapping("/{feedId}/comment")
    public CustomResponseBody saveFeedComment(@PathVariable Long feedId, @RequestBody FeedCommentDto.RequestDto requestDto, User user) {
        return feedCommentService.saveFeedComment(feedId, requestDto, user);
    }

    @PutMapping("/comment/{commentId}")
    public CustomResponseBody updateFeedComment(@PathVariable Long commentId, @RequestBody FeedCommentDto.RequestDto requestDto, User user) {
        return feedCommentService.updateFeedComment(commentId, requestDto, user);
    }

    @DeleteMapping("/comment/{commentId}")
    private CustomResponseBody deleteFeedComment(@PathVariable Long commentId, User user) {
        return feedCommentService.deleteFeedComment(commentId, user);
    }
}
