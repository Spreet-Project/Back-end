package com.team1.spreet.repository;

import com.team1.spreet.entity.EventComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventCommentRepository extends JpaRepository<EventComment, Long> {

	@Transactional
	@Modifying
	@Query("update EventComment ec set ec.isDeleted = true where ec.event.id = :eventId")
	void updateIsDeletedTrueByEventId(@Param("eventId") Long eventId);
}
