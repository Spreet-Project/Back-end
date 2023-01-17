package com.team1.spreet.repository;

import com.team1.spreet.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAll(Pageable pageable);



    Optional<Feed> findByUserIdAndId(Long userId, Long feedId);
}
