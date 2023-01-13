package com.team1.spreet.repository;

import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

	//카테고리별 조회
	Page<Shorts> findShortsByCategory(Category category, Pageable pageable);

	@Query("select distinct s from Shorts s join fetch s.user where s.id = :shortsId")
	Shorts findByIdWithUser(@Param("shortsId") Long shortsId);
}
