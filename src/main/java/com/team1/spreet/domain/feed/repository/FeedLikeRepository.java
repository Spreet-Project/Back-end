package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.Feed;
import com.team1.spreet.domain.feed.model.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    long countByFeedId(Long feedId);

    Optional<FeedLike> findByUserIdAndFeedId(Long userId, Long feedId);

    boolean existsByUserIdAndFeed(Long userId, Feed feed);

    void deleteByFeedId(Long feedId);
}
