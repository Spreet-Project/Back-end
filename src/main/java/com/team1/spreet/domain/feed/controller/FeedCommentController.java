package com.team1.spreet.domain.feed.controller;

import com.team1.spreet.domain.feed.dto.FeedCommentDto;
import com.team1.spreet.domain.feed.service.FeedCommentService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public CustomResponseBody<SuccessStatusCode> saveFeedComment(@PathVariable @ApiParam(value = "댓글 등록할 피드 ID") Long feedId,
                                                                 @RequestBody @Valid @ApiParam(value = "등록할 댓글 정보") FeedCommentDto.RequestDto requestDto) {
        feedCommentService.saveFeedComment(feedId, requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_COMMENT);
    }
    //댓글 수정
    @ApiOperation(value = "피드 댓글 수정 API")
    @PutMapping("/comment/{commentId}")
    public CustomResponseBody<SuccessStatusCode> updateFeedComment(@PathVariable @ApiParam(value = "댓글 수정할 피드 ID") Long commentId,
                                                                   @RequestBody @Valid @ApiParam(value = "수정할 댓글 정보") FeedCommentDto.RequestDto requestDto) {
        feedCommentService.updateFeedComment(commentId, requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_COMMENT);
    }
    //댓글 삭제
    @ApiOperation(value = "피드 댓글 삭제 API")
    @DeleteMapping("/comment/{commentId}")
    private CustomResponseBody<SuccessStatusCode> deleteFeedComment(@PathVariable @ApiParam(value = "댓글 삭제할 피드 ID") Long commentId) {
        feedCommentService.deleteFeedComment(commentId);
        return new CustomResponseBody<>(SuccessStatusCode.DELETE_COMMENT);
    }

    //댓글 조회
    @ApiOperation(value = "피드 댓글 조회 API")
    @GetMapping("/{feedId}/comment")
    private CustomResponseBody<List<FeedCommentDto.ResponseDto>> getFeedComment(@PathVariable @ApiParam(value = "댓글 리스트를 조회할 피드 ID") Long feedId) {
        return new CustomResponseBody<>(SuccessStatusCode.GET_COMMENTS,feedCommentService.getFeedComment(feedId));
    }
}
