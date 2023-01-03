package com.team1.spreet.repository;

import com.team1.spreet.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    long countByFeedIdAndIsLikeTrue(Long feedId);

    boolean existsByUserIdAndFeedId(Long userId, Long feedId);


}
