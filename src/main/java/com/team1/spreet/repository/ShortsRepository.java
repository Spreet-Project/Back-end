package com.team1.spreet.repository;

import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortsRepository extends JpaRepository<Shorts, Long> {
	@Query("select s from Shorts s where s.id = :shortsId and s.isDeleted = false")
	Optional<Shorts> findByIdAndIsDeletedFalse(@Param("shortsId")Long shortsId);

	@Query("select s from Shorts s join fetch s.user where s.id = :shortsId and s.isDeleted = false")
	Optional<Shorts> findByIdAndIsDeletedFalseWithUser(@Param("shortsId")Long shortsId);

	//카테고리별 조회
	List<Shorts> findShortsByIsDeletedFalseAndCategory(@Param("category")Category category, Pageable pageable);

	List<Shorts> findAllByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

	@Query(nativeQuery = true, value = "select * from ("
		+ "select s.shorts_id, s.title, s.created_at from shorts s where user_id = :userId union "
		+ "select f.feed_id, f.title, f.created_at from feed f where user_id = :userId union "
		+ "select e.event_id, e.title, e.created_at from event e where user_id = :userId) as p",
	countQuery = "select * from ("
		+ "select s.shorts_id, s.title, s.created_at from shorts s where user_id = :userId union "
		+ "select f.feed_id, f.title, f.created_at from feed f where user_id = :userId union "
		+ "select e.event_id, e.title, e.created_at from event e where user_id = :userId) as c")
	List<Shorts> findAllByUserIdAndIsDeletedFalseWithFeedAndEvent(@Param("userId") Long userId, Pageable pageable);
}
