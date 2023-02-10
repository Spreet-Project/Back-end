package com.team1.spreet.domain.alarm.controller;

import com.team1.spreet.domain.alarm.repository.EmitterRepository;
import com.team1.spreet.domain.alarm.service.AlarmService;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Api(tags = "sse")
@RequiredArgsConstructor
@RestController
public class SseController {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final AlarmService alarmService;
    //로그인 시 프론트에서 /sub 로 Get 요청 보냄(자바스크립트 EventSource 이용)
    @GetMapping(value = "/sub", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        /*
        sse 연결 요청에 대한 응답으로 emitter 를 만들어 반환한다
        DEFAULT_TIMEOUT 만큼 연결을 유지하고 시간이 지나면 클라이언트에서 재연결 요청을 보낸다 -> 왜 굳이 시간제한을 두지?
        * */
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        try {
            // 연결
            emitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //userNickname 를 키값으로 emitter 저장
        emitterRepository.save(user.getId(), new SseEmitter(DEFAULT_TIMEOUT));

        //시간 초과 및 요청이 정상 작동되지 않으면 emitter 를 삭제한다
        emitter.onCompletion(() -> emitterRepository.deleteByUserId(user.getId()));
        emitter.onTimeout(() -> emitterRepository.deleteByUserId(user.getId()));
        //수신하지 않은 알람 전송
        alarmService.getUnreadAlarm(user);
        return emitter;
    }
}
