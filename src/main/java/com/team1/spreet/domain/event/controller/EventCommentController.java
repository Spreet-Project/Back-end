package com.team1.spreet.domain.event.controller;

import com.team1.spreet.domain.event.dto.EventCommentDto;
import com.team1.spreet.domain.event.service.EventCommentService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "eventComment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventCommentController {

    private final EventCommentService eventCommentService;
    @ApiParam(value = "행사 댓글 등록 API")
    @PostMapping("/{eventId}/comment")
    public CustomResponseBody<SuccessStatusCode> saveEventComment(
        @PathVariable @ApiParam(value = "댓글 등록할 행사 ID") Long eventId,
        @RequestBody @Valid @ApiParam(value = "등록할 댓글 내용") EventCommentDto.RequestDto requestDto) {
        eventCommentService.saveEventComment(eventId, requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_COMMENT);
    }

    @ApiParam(value = "행사 댓글 수정 API")
    @PutMapping("/comment/{commentId}")
    public CustomResponseBody<SuccessStatusCode> updateEventComment(
        @PathVariable @ApiParam(value = "행사 댓글 ID") Long commentId,
        @RequestBody @Valid @ApiParam(value = "수정할 댓글 내용") EventCommentDto.RequestDto requestDto){
        eventCommentService.updateEventComment(commentId, requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_COMMENT);
    }

    @ApiParam(value = "행사 댓글 삭제 API")
    @DeleteMapping("/comment/{commentId}")
    public CustomResponseBody<SuccessStatusCode> deleteEventComment(
        @PathVariable @ApiParam(value = "행사 댓글 ID") Long commentId){
        eventCommentService.deleteEventComment(commentId);
        return new CustomResponseBody<>(SuccessStatusCode.DELETE_COMMENT);
    }

    @ApiParam(value = "행사 댓글 조회 API")
    @GetMapping("/{eventId}/comment")
    public CustomResponseBody<List<EventCommentDto.ResponseDto>> getEventCommentList(
        @PathVariable @ApiParam(value = "댓글 리스트를 조회할 행사 ID") Long eventId){
        return new CustomResponseBody<>(SuccessStatusCode.GET_COMMENTS, eventCommentService.getEventCommentList(eventId));
    }

}
