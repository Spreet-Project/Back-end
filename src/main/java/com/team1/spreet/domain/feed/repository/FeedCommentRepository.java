package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.FeedComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    @Query("select distinct fc from FeedComment fc join fetch fc.user where fc.feed.id = :feedId and fc.deleted = false order by fc.createdAt DESC")
    List<FeedComment> findByFeedIdAndOrderByCreatedAtDesc(@Param("feedId")Long feedId);

    @Transactional
    @Modifying
    @Query("update FeedComment f set f.deleted = true where f.feed.id = :feedId")
    void updateDeletedTrueByFeedId(@Param("feedId")Long feedId);
}
