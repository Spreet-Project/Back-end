package com.team1.spreet.service;

import com.team1.spreet.dto.AlertDto;
import com.team1.spreet.entity.Alert;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.repository.EmitterRepository;
import com.team1.spreet.repository.AlertRepository;
import com.team1.spreet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeIdIncludeTime(userId);
        /*
        sse 연결 요청에 대한 응답으로 emitter 를 만들어 반환한다
        DEFAULT_TIMEOUT 만큼 연결을 유지하고 시간이 지나면 클라이언트에서 재연결 요청을 보낸다 -> 왜 굳이 시간제한을 두지?
        userId가 포함된 emitterId를 키값으로 emitter 저장
        * */
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        //시간 초과 및 요청이 정상 작동되지 않으면 emitter 를 삭제한다
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        //503에러 발생 예방을 위한 더미 데이터 전송
        String eventId = makeIdIncludeTime(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방, lastEventId는 프론트에서 보내준다
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }
        return emitter;
    }

    public void send(Long userId, String content, String url) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
        Alert notification = notificationRepository.save(createNotification(content, url, false, user));

        String receiverId = String.valueOf(userId);
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, new AlertDto.ResponseDto(notification));
                }
        );
    }
    private Alert createNotification(String content, String url, boolean isRead, User user) {
        return new Alert(content, url, isRead, user);
    }

    private String makeIdIncludeTime(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }
    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public List<AlertDto.ResponseDto> getAllnotification(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
        List<AlertDto.ResponseDto> alertList = new ArrayList<>();
        List<Alert> alerts = alertRepository.findAllByReceiver(user.getNickname());
        for (Alert alert : alerts) {
            alertList.add(new AlertDto.ResponseDto(alert));
        }
        return alertList;
    }
}
