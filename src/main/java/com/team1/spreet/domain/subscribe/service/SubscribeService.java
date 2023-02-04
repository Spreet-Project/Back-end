package com.team1.spreet.domain.subscribe.service;

import com.team1.spreet.domain.subscribe.model.Subscribe;
import com.team1.spreet.domain.subscribe.repository.SubscribeRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    public void subscribe(String publisherNickname, User subscriber) {
        User publisher = checkUser(publisherNickname);

        // 본인 계정은 구독할 수 없도록
        if (publisher.getId().equals(subscriber.getId())) {
            throw new RestApiException(ErrorStatusCode.INVALID_SUBSCRIBE);
        }

        Subscribe subscribe = new Subscribe(publisher, subscriber);
        subscribeRepository.save(subscribe);
    }
    public void cancelSubscribe(String publisherNickname, User subscriber) {
        User publisher = checkUser(publisherNickname);
        subscribeRepository.deleteByPublisherAndSubscriber(publisher, subscriber);
    }
    private User checkUser(String userNickname){
        return userRepository.findByNickname(userNickname).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
    }
}
