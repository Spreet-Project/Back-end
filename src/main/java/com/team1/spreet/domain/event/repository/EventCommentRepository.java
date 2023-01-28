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
    @Query("select ec from EventComment ec where ec.id = :eventCommentId and ec.isDeleted = false")
    Optional<EventComment> findByIdAndIsDeletedFalse(@Param("eventCommentId") Long eventCommentId);

    @Transactional
    @Modifying
    @Query("update EventComment e set e.content = :content where e.id = :commentId and e.isDeleted = false")
    void updateContentByIdAndIsDeletedFalse(@Param("content") String content, @Param("commentId") Long commentId);

    @Transactional
    @Modifying
    @Query("update EventComment ec set ec.isDeleted = True where ec.id = :commentId")
    void updateIsDeletedById(@Param("commentId") Long commentId);

    @Query("select distinct ec, ec.user.nickname from EventComment ec join fetch ec.user " +
            "where ec.event.id = :eventId and ec.isDeleted = false order by ec.createdAt DESC")
    List<EventComment> findByEventIdWithUserAndIsDeletedFalseOrderByCreatedAtDesc(@Param("eventId") Long eventId);





	@Transactional
	@Modifying
	@Query("update EventComment ec set ec.isDeleted = true where ec.event.id = :eventId")
	void updateIsDeletedTrueByEventId(@Param("eventId") Long eventId);
}
