package com.team1.spreet.domain.alert.service;

import com.team1.spreet.domain.alert.dto.AlertDto;
import com.team1.spreet.domain.alert.model.Alert;
import com.team1.spreet.domain.alert.repository.AlertRepository;
import com.team1.spreet.domain.alert.repository.EmitterRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final EmitterRepository emitterRepository;
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

//로그인 시 미수신 알람을 보낸다
    public void alertEvent(User user) {
        String userNickname = user.getNickname();
        Map<String, SseEmitter> emitters = emitterRepository.findAll();
        if(emitters.containsKey(userNickname)){
            SseEmitter emitter = emitters.get(userNickname);
            try {
                List<Alert> alerts = alertRepository.findAllByReceiverAndIsReadFalse(user.getNickname());
                for (Alert alert : alerts) {
                    if (!alert.isRead()) {
                        emitter.send(SseEmitter
                                .event()
                                .name("미수신 알림")
                                .data(alert));
                    }
                }
            } catch (Exception e) {
                emitters.remove(userNickname);
            }
        }
    }
    public void send(Long senderId, String content, String url, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        User receiver = userRepository.findById(receiverId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        Alert alert = alertRepository.save(new Alert(content, url, false, sender.getNickname(), receiver.getNickname()));
//        String receiverId = String.valueOf(userId);
//        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserNickname(receiver.getNickname());
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, alert);
                    sendAlert(emitter, key, new AlertDto.ResponseDto(alert));
                }
        );
    }
//    private Alert createAlert(String content, String url, boolean isRead, String sender, String receiver) {
//        return new Alert(content, url, isRead, sender, receiver);
//    }
    private void sendAlert(SseEmitter emitter, String receiverNickname, Object alertResponseDto) {
        try {
            emitter.send(SseEmitter.event()
                    .data(alertResponseDto));
        } catch (IOException exception) {
            emitterRepository.deleteByUserNickname(receiverNickname);
        }
    }
//    private boolean hasLostData(String lastEventId) {
//        return !lastEventId.isEmpty();
//    }
//    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserNickname(String.valueOf(userId));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendAlert(emitter, entry.getKey(), entry.getValue()));
//    }
    @Transactional(readOnly = true)
    public List<AlertDto.ResponseDto> getAllAlert(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        List<AlertDto.ResponseDto> alertList = new ArrayList<>();
        List<Alert> alerts = alertRepository.findAllByReceiverAndIsReadFalse(user.getNickname());
        for (Alert alert : alerts) {
            alertList.add(new AlertDto.ResponseDto(alert));
        }
        return alertList;
    }
    @Transactional
    public SuccessStatusCode ReadAlert(Long alertId, UserDetailsImpl userDetails) {
        Alert alert = alertRepository.findById(alertId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_ALERT)
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        if(!alert.getReceiver().equals(user.getNickname())){
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        }
        alert.ReadAlert();
        alertRepository.delete(alert);
        return SuccessStatusCode.READ_ALERT;
    }
}
