package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.dto.EventCommentDto;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.EventCommentService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public CustomResponseBody<SuccessStatusCode> saveEventComment(@PathVariable @ApiParam(value = "댓글 등록할 행사 ID") Long eventId,
                                               @RequestBody @Valid @ApiParam(value = "등록할 댓글 내용") EventCommentDto.RequestDto requestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(eventCommentService.saveEventComment(eventId, requestDto, userDetails));
    }
    @ApiParam(value = "행사 댓글 수정 API")
    @PutMapping("/comment/{commentId}")
    public CustomResponseBody<SuccessStatusCode> updateEventComment(@PathVariable @ApiParam(value = "행사 댓글 ID") Long commentId,
                                                 @RequestBody @Valid @ApiParam(value = "수정할 댓글 내용") EventCommentDto.RequestDto requestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new CustomResponseBody<>(eventCommentService.updateEventComment(commentId, requestDto, userDetails));
    }
    @ApiParam(value = "행사 댓글 삭제 API")
    @DeleteMapping("/comment/{commentId}")
    public CustomResponseBody<SuccessStatusCode> deleteEventComment(@PathVariable @ApiParam(value = "행사 댓글 ID") Long commentId,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new CustomResponseBody<>(eventCommentService.deleteEventComment(commentId, userDetails));
    }
    @ApiParam(value = "행사 댓글 조회 API")
    @GetMapping("/{eventId}/comment")
    public CustomResponseBody<List<EventCommentDto.ResponseDto>> getEventCommentList(@PathVariable @ApiParam(value = "댓글 리스트를 조회할 행사 ID") Long eventId){
        return new CustomResponseBody<>(SuccessStatusCode.GET_COMMENTS, eventCommentService.getEventCommentList(eventId));
    }

}
