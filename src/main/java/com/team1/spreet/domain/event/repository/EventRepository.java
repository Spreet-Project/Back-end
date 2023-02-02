package com.team1.spreet.domain.event.repository;

import com.team1.spreet.domain.event.model.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long>, EventCustomRepository {

	Optional<Event> findByIdAndDeletedFalse(Long eventId);

	@Query("select e from Event e join fetch e.user where e.id = :eventId and e.deleted = false")
	Optional<Event> findByIdAndDeletedFalseWithUser(@Param("eventId") Long eventId);

	List<Event> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);

}
