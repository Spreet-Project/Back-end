package com.team1.spreet.controller;

import com.team1.spreet.dto.AlertDto;
import com.team1.spreet.dto.CustomResponseBody;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.security.UserDetailsImpl;
import com.team1.spreet.service.AlertService;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "alert")
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
