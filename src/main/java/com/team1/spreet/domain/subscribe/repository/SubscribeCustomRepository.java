package com.team1.spreet.domain.subscribe.repository;

import com.team1.spreet.domain.mypage.dto.MyPageDto;
import java.util.List;

public interface SubscribeCustomRepository {

    void deleteByUserId(Long userId);

    List<MyPageDto.SubscribeInfoDto> findAllBySubscriberId(Long subscriberId, Long page);
}
