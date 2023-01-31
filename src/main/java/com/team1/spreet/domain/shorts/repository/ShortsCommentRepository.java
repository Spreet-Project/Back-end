package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.model.ShortsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
    @Query("select distinct sc, sc.user from ShortsComment sc join fetch sc.user "
        + "where sc.shorts.id = :shortsId and sc.deleted = false order by sc.createdAt desc")
    List<ShortsComment> findByShortsIdAndDeletedFalseWithUserOrderByCreatedAtDesc(@Param("shortsId") Long shortsId);

    @Query("select distinct sc from ShortsComment sc join fetch sc.user where sc.id = :shortsCommentId and sc.deleted = false")
    Optional<ShortsComment> findByIdAndDeletedFalseWithUser(@Param("shortsCommentId") Long shortsCommentId);

    @Transactional
    @Modifying
    @Query("update ShortsComment sc set sc.deleted = true where sc.shorts.id = :shortsId")
    void updateDeletedTrueByShortsId(@Param("shortsId") Long shortsId);
}
