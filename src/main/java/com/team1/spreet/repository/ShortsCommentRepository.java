package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
    @Query("select distinct sc, sc.user.nickname from ShortsComment sc join fetch sc.user "
        + "where sc.shorts.id = :shortsId and sc.isDeleted = false order by sc.createdAt desc")
    List<ShortsComment> findByShortsIdAndIsDeletedFalseWithUserOrderByCreatedAtDesc(@Param("shortsId") Long shortsId);

    @Query("select sc from ShortsComment sc where sc.id = :shortsCommentId and sc.isDeleted = false")
    Optional<ShortsComment> findByIdAndIsDeletedFalse(@Param("shortsCommentId") Long shortsCommentId);

    @Modifying
    @Query("update ShortsComment sc set sc.isDeleted = true where sc.id = :shortsCommentId")
    void updateIsDeletedTrueById(@Param("shortsCommentId") Long shortsCommentId);

    @Transactional
    @Modifying
    @Query("update ShortsComment sc set sc.isDeleted = true where sc.shorts.id = :shortsId")
    void updateIsDeletedTrueByShortsId(@Param("shortsId") Long shortsId);

}
