package com.team1.spreet.repository;

import com.team1.spreet.entity.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<FeedImage, Long> {
    List<FeedImage> findByFeedId(Long feedId);

}
