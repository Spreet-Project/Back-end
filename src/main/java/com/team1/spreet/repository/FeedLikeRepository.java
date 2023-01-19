package com.team1.spreet.repository;

import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.FeedLike;
import com.team1.spreet.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    long countByFeedId(Long feedId);

    Optional<FeedLike> findByUserAndFeed(User user, Feed feed);

    boolean existsByUserIdAndFeed(Long userId, Feed feed);

    @Transactional
    @Modifying
    @Query("delete from FeedLike f where f.feed = :feed")
    void deleteByFeed(@Param("feed")Feed feed);




}
