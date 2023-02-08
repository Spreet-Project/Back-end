package com.team1.spreet.domain.subscribe.repository;

import com.team1.spreet.domain.subscribe.model.Subscribe;
import com.team1.spreet.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>, SubscribeCustomRepository {
    Optional<List<Subscribe>> findByPublisher(User publisher);

    Optional<Subscribe> findByPublisherIdAndSubscriberId(Long publisherId, Long subscriberId);


}
