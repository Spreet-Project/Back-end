package com.team1.spreet.repository;

import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.FeedLike;
import com.team1.spreet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    long countByFeedId(Long feedId);

    Optional<FeedLike> findByUserAndFeed(User user, Feed feed);

    boolean existsByUserAndFeed(User user, Feed feed);


}
