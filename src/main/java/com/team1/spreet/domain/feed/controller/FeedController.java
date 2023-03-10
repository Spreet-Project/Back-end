package com.team1.spreet.domain.feed.controller;

import com.team1.spreet.domain.feed.dto.FeedDto;
import com.team1.spreet.domain.feed.service.FeedService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "feed")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;

    //feed 최신순 조회
    @ApiOperation(value = "피드 최신순 조회 API")
    @GetMapping("")
    public CustomResponseBody<List<FeedDto.ResponseDto>> getSortedFeed(
            @RequestParam(value = "sort", defaultValue = "popular") @ApiParam(value = "정렬방법") String sort,
            @RequestParam(value ="page") @ApiParam(value = "조회할 페이지") Long page,
            @RequestParam(value = "size") @ApiParam(value = "조회할 사이즈") Long size){
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getSortedFeed(sort, page, size));
    }
    @ApiOperation(value = "메인 화면에서 feed 조회 API")
    @GetMapping("/main")
    public CustomResponseBody<List<FeedDto.SimpleResponseDto>> getSimpleFeed(){
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getSimpleFeed());
    }
    //feed 조회
    @ApiOperation(value = "피드 상세조회 API")
    @GetMapping("/{feedId}")
    public CustomResponseBody<FeedDto.ResponseDto> getFeed(@PathVariable @ApiParam(value = "조회할 피드 ID") Long feedId) {
        return new CustomResponseBody<>(SuccessStatusCode.GET_FEED, feedService.getFeed(feedId));
    }
    //feed 저장
    @ApiOperation(value = "피드 등록 API")
    @PostMapping("")
    public CustomResponseBody<SuccessStatusCode> saveFeed(@ModelAttribute @Valid @ApiParam(value = "피드 등록 정보") FeedDto.RequestDto requestDto){
        feedService.saveFeed(requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.SAVE_FEED);
    }
    //feed 수정
    @ApiOperation(value = "피드 수정 API")
    @PutMapping("/{feedId}")
    public CustomResponseBody<SuccessStatusCode> updateFeed(@PathVariable @ApiParam(value = "수정할 피드 ID") Long feedId,
        @ModelAttribute @Valid @ApiParam(value = "피드 수정 정보") FeedDto.RequestDto requestDto){
        feedService.updateFeed(feedId, requestDto);
        return new CustomResponseBody<>(SuccessStatusCode.UPDATE_FEED);
    }
    //feed 삭제
    @DeleteMapping("/{feedId}")
    @ApiOperation(value = "피드 삭제 API")
    public CustomResponseBody<SuccessStatusCode> deleteFeed(@PathVariable @ApiParam(value = "삭제할 피드 ID") Long feedId){
        feedService.deleteFeed(feedId);
        return new CustomResponseBody<>(SuccessStatusCode.DELETE_FEED);
    }

}
