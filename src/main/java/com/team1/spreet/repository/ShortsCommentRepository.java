package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
    @Query("select distinct sc from ShortsComment sc join fetch sc.user where sc.shorts.id = :shortsId order by sc.createdAt desc")
    List<ShortsComment> findByShortsIdWithUserOrderByCreatedAtDesc(@Param("shortsId") Long shortsId);
}
