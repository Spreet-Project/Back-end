package com.team1.spreet.repository;

import com.team1.spreet.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreatedAtDesc(Pageable pageable);

    
}
