package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long>, FeedLikeCustomRepository {

    Optional<FeedLike> findByUserIdAndFeedId(Long userId, Long feedId);

    void deleteByFeedId(Long feedId);

    void deleteByUserId(Long userId);
}
