package com.team1.spreet.domain.subscribe.controller;

import com.team1.spreet.domain.subscribe.dto.SubscribeDto;
import com.team1.spreet.domain.subscribe.service.SubscribeService;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "subscribe")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;
    @PostMapping
    public CustomResponseBody<SuccessStatusCode> subscribe(@RequestBody @ApiParam(value = "구독을 요청한 회원 ID") SubscribeDto.RequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        subscribeService.subscribe(requestDto.getNickname(), userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.SUBSCRIBE);
    }
    @DeleteMapping
    public CustomResponseBody<SuccessStatusCode> cancelSubscribe(@RequestBody @ApiParam(value = "구독 취소를 요청한 회원 ID") SubscribeDto.RequestDto requestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        subscribeService.cancelSubscribe(requestDto.getNickname(), userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.CANCEL_SUBSCRIBE);
    }
}
