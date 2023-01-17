package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.FeedCommentDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.FeedCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "feedComment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;
    //댓글 생성
    @ApiOperation(value = "피드 댓글 등록 API")
    @PostMapping("/{feedId}/comment")
    public CustomResponseBody<FeedCommentDto.ResponseDto> saveFeedComment(@PathVariable @ApiParam(value = "댓글 등록할 피드 ID") Long feedId,
        @RequestBody @ApiParam(value = "등록할 댓글 정보") FeedCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_FEED_COMMENT,feedCommentService.saveFeedComment(feedId, requestDto, userDetails));
    }
    //댓글 수정
    @ApiOperation(value = "피드 댓글 수정 API")
    @PutMapping("/comment/{commentId}")
    public CustomResponseBody<FeedCommentDto.ResponseDto> updateFeedComment(@PathVariable @ApiParam(value = "댓글 수정할 피드 ID") Long commentId,
        @RequestBody @ApiParam(value = "수정할 댓글 정보") FeedCommentDto.RequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_FEED_COMMENT, feedCommentService.updateFeedComment(commentId, requestDto, userDetails));
    }
    //댓글 삭제
    @ApiOperation(value = "피드 댓글 삭제 API")
    @DeleteMapping("/comment/{commentId}")
    private CustomResponseBody<SuccessStatusCode> deleteFeedComment(@PathVariable @ApiParam(value = "댓글 삭제할 피드 ID") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(feedCommentService.deleteFeedComment(commentId, userDetails));
    }

    //댓글 조회
    @ApiOperation(value = "피드 댓글 조회 API")
    @GetMapping("/{feedId}/comment")
    private CustomResponseBody<List<FeedCommentDto.ResponseDto>> getFeedComment(@PathVariable @ApiParam(value = "댓글 리스트를 조회할 피드 ID") Long feedId) {
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED_COMMENTS,feedCommentService.getFeedComment(feedId));
    }
}
