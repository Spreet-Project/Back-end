package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.model.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("select distinct e, e.user.id from Event e join fetch e.user "
		+ "where e.id = :eventId and e.isDeleted = false")
	Optional<Event> findByIdAndIsDeletedFalse(@Param("eventId") Long eventId);

	@Transactional
	@Modifying
	@Query("update Event e set e.isDeleted = true where e.id = :eventId")
	void updateIsDeletedTrue(@Param("eventId") Long eventId);

	@Query("select distinct e from Event e join fetch e.user where e.isDeleted = false order by e.createdAt desc")
	List<Event> findAllByIsDeletedFalseWithUserOrderByCreatedAtDesc();

	List<Event> findAllByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
}
