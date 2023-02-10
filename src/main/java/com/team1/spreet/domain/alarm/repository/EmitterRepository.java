package com.team1.spreet.domain.alarm.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        emitters.put(userId, sseEmitter);
        return sseEmitter;
    }
    public Map<Long, SseEmitter> findAll(){
        return emitters;
    }
    public Map<Long, SseEmitter> findAllByUserId(Long userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().equals(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public void deleteByUserId(Long userId) {
        emitters.remove(userId);
    }

    public void deleteAllEmitterStartWithId(Long memberId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.equals(memberId)) {
                        emitters.remove(key);
                    }
                }
        );
    }
}
