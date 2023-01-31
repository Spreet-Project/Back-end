package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.model.EventComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface EventCommentRepository extends JpaRepository<EventComment, Long> {

    Optional<EventComment> findByIdAndDeletedFalse(@Param("eventCommentId") Long eventCommentId);

    @Query("select distinct ec, ec.user.nickname from EventComment ec join fetch ec.user " +
            "where ec.event.id = :eventId and ec.deleted = false order by ec.createdAt DESC")
    List<EventComment> findByEventIdWithUserAndDeletedFalseOrderByCreatedAtDesc(@Param("eventId") Long eventId);

	@Transactional
	@Modifying
	@Query("update EventComment ec set ec.deleted = true where ec.event.id = :eventId")
	void updateDeletedTrueByEventId(@Param("eventId") Long eventId);

}
