package com.team1.spreet.domain.subscribe.service;

import com.team1.spreet.domain.subscribe.model.Subscribe;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.domain.subscribe.repository.SubscribeRepository;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    public SuccessStatusCode subscribe(Long publisherId, UserDetailsImpl userDetails) {
        User publisher = userRepository.findById(publisherId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        User subscriber = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        Subscribe subscribe = new Subscribe(publisher, subscriber);
        subscribeRepository.save(subscribe);
        return SuccessStatusCode.SUBSCRIBE;
    }
    public SuccessStatusCode cancelSubscribe(Long publisherId, UserDetailsImpl userDetails) {
        User publisher = userRepository.findById(publisherId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        User subscriber = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
        Subscribe subscribe = subscribeRepository.findByPublisherAndSubscriber(publisher, subscriber);
        subscribeRepository.delete(subscribe);
        return SuccessStatusCode.CANCEL_SUBSCRIBE;
    }
}
