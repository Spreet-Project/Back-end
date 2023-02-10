package com.team1.spreet.domain.subscribe.controller;

import com.team1.spreet.domain.subscribe.dto.SubscribeDto;
import com.team1.spreet.domain.subscribe.service.SubscribeService;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "subscribe")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;
    @PostMapping
    public CustomResponseBody<SuccessStatusCode> subscribe(@ApiParam(value = "구독 요청 또는 취소한 회원 nickname") @RequestBody SubscribeDto.RequestDto requestDto) {
        return new CustomResponseBody<>(subscribeService.subscribe(requestDto.getNickname()));
    }
}
