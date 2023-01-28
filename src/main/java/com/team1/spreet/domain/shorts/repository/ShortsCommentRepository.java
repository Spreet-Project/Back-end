package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.model.ShortsComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
    @Query("select distinct sc, sc.user.nickname from ShortsComment sc join fetch sc.user "
        + "where sc.shorts.id = :shortsId and sc.isDeleted = false order by sc.createdAt desc")
    List<ShortsComment> findByShortsIdAndIsDeletedFalseWithUserOrderByCreatedAtDesc(@Param("shortsId") Long shortsId);

    @Query("select distinct sc from ShortsComment sc join fetch sc.user where sc.id = :shortsCommentId and sc.isDeleted = false")
    Optional<ShortsComment> findByIdAndIsDeletedFalseWithUser(@Param("shortsCommentId") Long shortsCommentId);

    @Transactional
    @Modifying
    @Query("update ShortsComment sc set sc.isDeleted = true where sc.shorts.id = :shortsId")
    void updateIsDeletedTrueByShortsId(@Param("shortsId") Long shortsId);

}
