package com.team1.spreet.repository;

import com.team1.spreet.entity.Subscribe;
import com.team1.spreet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    Optional<List<Subscribe>> findAllByPublisher(User publisher);

    Subscribe findByPublisherAndSubscriber(User publisher, User subscriber);
}
