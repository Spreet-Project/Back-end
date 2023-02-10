package com.team1.spreet.domain.subscribe.service;

import com.team1.spreet.domain.subscribe.model.Subscribe;
import com.team1.spreet.domain.subscribe.repository.SubscribeRepository;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.repository.UserRepository;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;
    @Transactional
    public SuccessStatusCode subscribe(String publisherNickname) {
        User subscriber = SecurityUtil.getCurrentUser();
        if(subscriber == null){
            throw new RestApiException(ErrorStatusCode.NOT_EXIST_AUTHORIZATION);
        }
        User publisher = checkUser(publisherNickname);

        // 본인 계정은 구독할 수 없도록
        if (publisher.getId().equals(subscriber.getId())) {
            throw new RestApiException(ErrorStatusCode.INVALID_SUBSCRIBE);
        }
        Subscribe subscribe = subscribeRepository.findByPublisherIdAndSubscriberId(publisher.getId(), subscriber.getId()).orElse(null);
        if(subscribe == null){
            Subscribe newSubscribe = new Subscribe(publisher, subscriber);
            subscribeRepository.save(newSubscribe);
            return SuccessStatusCode.SUBSCRIBE;
        }else{
            subscribeRepository.delete(subscribe);
            return SuccessStatusCode.CANCEL_SUBSCRIBE;
        }

    }
    private User checkUser(String userNickname){
        return userRepository.findByNickname(userNickname).orElseThrow(
                ()-> new RestApiException(ErrorStatusCode.NOT_EXIST_USER)
        );
    }
}
