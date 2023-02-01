package com.team1.spreet.domain.subscribe.controller;

import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.domain.subscribe.service.SubscribeService;
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
    @PostMapping("/{publisherId}")
    public CustomResponseBody<SuccessStatusCode> subscribe(@ApiParam(value = "구독을 요청한 회원 ID") @PathVariable Long publisherId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(subscribeService.subscribe(publisherId, userDetails));
    }
    @DeleteMapping("/{publisherId}")
    public CustomResponseBody<SuccessStatusCode> cancelSubscribe(@ApiParam(value = "구독 취소를 요청한 회원 ID") @PathVariable Long publisherId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(subscribeService.cancelSubscribe(publisherId, userDetails));
    }
}
