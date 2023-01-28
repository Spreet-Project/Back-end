package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {
    List<FeedImage> findByFeedId(Long feedId);

}
