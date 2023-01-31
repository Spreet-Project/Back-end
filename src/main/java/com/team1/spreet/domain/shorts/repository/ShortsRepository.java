package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.model.Category;
import com.team1.spreet.domain.shorts.model.Shorts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortsRepository extends JpaRepository<Shorts, Long> {

	Optional<Shorts> findByIdAndDeletedFalse(Long shortsId);

	@Query("select s from Shorts s join fetch s.user where s.id = :shortsId and s.deleted = false")
	Optional<Shorts> findByIdAndDeletedFalseWithUser(@Param("shortsId")Long shortsId);

	//카테고리별 조회
	List<Shorts> findShortsByDeletedFalseAndCategory(Category category, Pageable pageable);

	List<Shorts> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);

}
