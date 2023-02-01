package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.model.ShortsComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long>, ShortsCommentCustomRepository {

    @Query("select distinct sc from ShortsComment sc join fetch sc.user where sc.id = :shortsCommentId and sc.deleted = false")
    Optional<ShortsComment> findByIdAndDeletedFalseWithUser(@Param("shortsCommentId") Long shortsCommentId);

}
