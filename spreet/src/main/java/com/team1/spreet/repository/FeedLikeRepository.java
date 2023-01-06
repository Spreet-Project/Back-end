package com.team1.spreet.repository;

import com.team1.spreet.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    long countByFeedIdAndIsLikeTrue(Long feedId);

    FeedLike findByUserIdAndFeedId(Long userId, Long feedId);
}
