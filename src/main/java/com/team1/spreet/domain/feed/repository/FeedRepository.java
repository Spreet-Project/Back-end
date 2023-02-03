package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedCustomRepository {

    List<Feed> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);
}
