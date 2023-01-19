package com.team1.spreet.controller;

import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;
    @PostMapping("/{publisherId}")
    public CustomResponseBody<SuccessStatusCode> subscribe(@PathVariable Long publisherId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(subscribeService.subscribe(publisherId, userDetails));
    }
    @DeleteMapping("/{publisherId}")
    public CustomResponseBody<SuccessStatusCode> cancelSubscribe(@PathVariable Long publisherId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new CustomResponseBody<>(subscribeService.cancelSubscribe(publisherId, userDetails));
    }
}
