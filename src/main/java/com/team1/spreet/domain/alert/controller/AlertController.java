package com.team1.spreet.domain.alert.controller;

import com.team1.spreet.domain.alert.dto.AlertDto;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.domain.alert.service.AlertService;
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
