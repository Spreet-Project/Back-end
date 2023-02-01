package com.team1.spreet.domain.alarm.service;

import com.team1.spreet.domain.alarm.dto.AlarmDto;
import com.team1.spreet.domain.alarm.model.Alarm;
import com.team1.spreet.domain.alarm.repository.AlarmRepository;
import com.team1.spreet.domain.alarm.repository.EmitterRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

//로그인 시 미수신 알람을 보낸다
    public void getUnreadAlarm(User user) {
        Long userId = user.getId();
        Map<Long, SseEmitter> emitters = emitterRepository.findAll();
        if(emitters.containsKey(userId)){
            SseEmitter emitter = emitters.get(userId);
            try {
                List<Alarm> alarms = alarmRepository.findAllByReceiverIdAndReadFalse(user.getId());
                for (Alarm alarm : alarms) {
                    if (!alarm.isRead()) {
                        emitter.send(SseEmitter
                                .event()
                                .name("미수신 알림")
                                .data(alarm));
                    }
                }
            } catch (Exception e) {
                emitters.remove(userId);
            }
        }
    }
    public void send(Long senderId, String content, String url, Long receiverId) {
        User sender = checkUser(senderId);
        User receiver = checkUser(receiverId);
        Alarm alarm = alarmRepository.save(new Alarm(content, url, false, sender, receiver));
        Map<Long, SseEmitter> emitters = emitterRepository.findAllByUserId(receiver.getId());
        for (Map.Entry<Long, SseEmitter> entry : emitters.entrySet()) {
            Long id = entry.getKey();
            SseEmitter emitter = entry.getValue();
            sendAlarm(emitter, id, new AlarmDto.ResponseDto(alarm));
        }
    }
    private void sendAlarm(SseEmitter emitter, Long receiverId, AlarmDto.ResponseDto alertResponseDto) {
        try {
            emitter.send(SseEmitter
                    .event()
                    .name("new alarm")
                    .data(alertResponseDto));
        } catch (IOException exception) {
            emitterRepository.deleteByUserId(receiverId);
        }
    }
    @Transactional(readOnly = true)
    public List<AlarmDto.ResponseDto> getAllAlarm(User user) {
        List<AlarmDto.ResponseDto> alarmList = new ArrayList<>();
        List<Alarm> alarms = alarmRepository.findAllByReceiverIdAndReadFalse(user.getId());
        for (Alarm alarm : alarms) {
            alarmList.add(new AlarmDto.ResponseDto(alarm));
        }
        return alarmList;
    }
    @Transactional
    public void readAlarm(Long alarmId, User user) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_ALARM)
        );
        if(!alarm.getReceiver().getId().equals(user.getId())){
            throw new RestApiException(ErrorStatusCode.UNAVAILABLE_MODIFICATION);
        }
        alarm.readAlarm();
        alarmRepository.delete(alarm);
    }
    private User checkUser(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
    }
}
