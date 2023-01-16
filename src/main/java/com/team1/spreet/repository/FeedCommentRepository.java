package com.team1.spreet.repository;

import com.team1.spreet.entity.FeedComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    List<FeedComment> findAllByFeedId(Long feedId);

    @Query("select distinct fc, fc.user.nickname from FeedComment fc join fetch fc.user where fc.feed.id = :feedId and fc.isDeleted = false order by fc.createdAt DESC")
    List<FeedComment> findByFeedIdAndIsDeletedFalseOrderByCreatedAtDesc(@Param("feedId")Long feedId);


    FeedComment findByUserIdAndId(Long userId, Long Id);

}
