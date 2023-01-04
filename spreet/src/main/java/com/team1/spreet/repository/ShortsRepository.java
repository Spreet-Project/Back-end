package com.team1.spreet.repository;

import com.team1.spreet.entity.Shorts;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {

	//카테고리별 조회(작성일자 내림차순 정렬)
	@Query(nativeQuery = true, value = "select * from shorts s where is_deleted=false "
		+ "and s.category = :category order by s.created_at desc")
	Page<Shorts> findShortsByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

	Optional<Shorts> findByIdAndDeletedFalse(Long aLong);
}
