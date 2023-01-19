package com.team1.spreet.controller;

import com.team1.spreet.dto.AlertDto;
import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/alerts")
    public CustomResponseBody<List<AlertDto.ResponseDto>> getAllAlert(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new CustomResponseBody<>(SuccessStatusCode.GET_ALERTS,alertService.getAllAlert(userDetails));
    }
    @PostMapping("/alert/{alertId}")
    public CustomResponseBody<SuccessStatusCode> ReadAlert(@PathVariable Long alertId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new CustomResponseBody<>(alertService.ReadAlert(alertId, userDetails));
    }
}
