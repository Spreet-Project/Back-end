package com.team1.spreet.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String userNickname, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);
    Map<String, SseEmitter> findAll();
    Map<String, SseEmitter> findAllEmitterStartWithByUserNickname(String userNickname);
    Map<String, Object> findAllEventCacheStartWithByUserNickname(String userNickname);
    void deleteByUserNickname(String userNickname);
    void deleteAllEmitterStartWithId(String userNickname);
    void deleteAllEventCacheStartWithId(String memberId);
}