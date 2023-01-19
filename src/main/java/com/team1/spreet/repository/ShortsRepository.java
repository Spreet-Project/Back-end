package com.team1.spreet.repository;

import com.team1.spreet.entity.Category;
import com.team1.spreet.entity.Shorts;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {
	@Query("select s from Shorts s where s.id = :shortsId and s.isDeleted = false")
	Optional<Shorts> findByIdAndIsDeletedFalse(@Param("shortsId")Long shortsId);

	@Modifying
	@Query("update Shorts s set s.isDeleted = true where s.id = :shortsId")
	void updateIsDeletedTrueById(@Param("shortsId") Long shortsId);

	//카테고리별 조회
	@Query("select s from Shorts s where s.category = :category and s.isDeleted = false")
	Page<Shorts> findShortsByCategoryAndIsDeletedFalse(@Param("category")Category category, Pageable pageable);

}
