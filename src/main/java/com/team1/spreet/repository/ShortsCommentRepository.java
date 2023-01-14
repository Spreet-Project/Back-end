package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
    @Query("select distinct sc, sc.user.nickname from ShortsComment sc join fetch sc.user "
        + "where sc.shorts.id = :shortsId and sc.shorts.isDeleted = false order by sc.createdAt desc")
    List<ShortsComment> findByShortsIdWithUserAndDeletedIsFalseOrderByCreatedAtDesc(@Param("shortsId") Long shortsId);

    @Query("select distinct sc from ShortsComment sc where sc.id = :shortsCommentId and sc.isDeleted = false")
    Optional<ShortsComment> findByIdAndDeletedIsFalse(Long shortsCommentId);

    @Modifying
    @Query("update ShortsComment sc SET sc.isDeleted = true WHERE sc.id = :shortsCommentId")
    void updateDeletedIsTrue(@Param("shortsCommentId") Long shortsCommentId);

    @Modifying
    @Query("update ShortsComment sc SET sc.isDeleted = true WHERE sc.id in :shortsCommentIds")
    void updateDeletedIsTrueByIds(@Param("shortsCommentIds") List<Long> shortsCommentIds);

    @Query("select sc.id from ShortsComment sc where sc.shorts.id = :shortsId and sc.isDeleted = false")
    List<Long> findIdsByShortId(@Param("shortsId") Long shortsId);
}
