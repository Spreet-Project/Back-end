package com.team1.spreet.domain.alarm.controller;

import com.team1.spreet.domain.alarm.dto.AlarmDto;
import com.team1.spreet.domain.alarm.service.AlarmService;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.global.common.dto.CustomResponseBody;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "alarm")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public CustomResponseBody<List<AlarmDto.ResponseDto>> getAllAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new CustomResponseBody<>(SuccessStatusCode.GET_ALARM,alarmService.getAllAlarm(userDetails.getUser()));
    }
    @PostMapping("/{alarmId}")
    public CustomResponseBody<SuccessStatusCode> readAlarm(@PathVariable Long alarmId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        alarmService.readAlarm(alarmId, userDetails.getUser());
        return new CustomResponseBody<>(SuccessStatusCode.READ_ALARM);
    }
}
