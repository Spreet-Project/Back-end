package com.team1.spreet.repository;

import com.team1.spreet.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    List<FeedComment> findAllByFeedId(Long feedId);

    FeedComment findByUserIdAndId(Long userId, Long Id);


}
