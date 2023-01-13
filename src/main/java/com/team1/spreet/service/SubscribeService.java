package com.team1.spreet.service;

import com.team1.spreet.entity.Subscribe;
import com.team1.spreet.entity.User;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import com.team1.spreet.exception.SuccessStatusCode;
import com.team1.spreet.repository.SubscribeRepository;
import com.team1.spreet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    public SuccessStatusCode subscribe(Long publisherId, long subscriberId) {
        User publisher = userRepository.findById(publisherId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
        User subscriber = userRepository.findById(subscriberId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
        Subscribe subscribe = new Subscribe(publisher, subscriber);
        subscribeRepository.save(subscribe);
        return SuccessStatusCode.SUBSCRIBE;
    }
    public SuccessStatusCode cancelSubscribe(Long publisherId, long subscriberId) {
        User publisher = userRepository.findById(publisherId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
        User subscriber = userRepository.findById(subscriberId).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NULL_USER_ID_DATA_EXCEPTION)
        );
        Subscribe subscribe = subscribeRepository.findByPublisherAndSubscriber(publisher, subscriber);
        subscribeRepository.delete(subscribe);
        return SuccessStatusCode.CANCEL_SUBSCRIBE;
    }
}
