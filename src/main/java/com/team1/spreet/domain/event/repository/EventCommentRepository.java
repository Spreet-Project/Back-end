package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.model.EventComment;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventCommentRepository extends JpaRepository<EventComment, Long>, EventCommentCustomRepository {

    @Query("select distinct ec from EventComment ec join fetch ec.user where ec.id = :eventCommentId and ec.deleted = false")
    Optional<EventComment> findByIdAndDeletedFalseWithUser(@Param("eventCommentId") Long eventCommentId);
}
