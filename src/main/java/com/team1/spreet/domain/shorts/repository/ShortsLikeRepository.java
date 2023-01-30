package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.model.ShortsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShortsLikeRepository  extends JpaRepository<ShortsLike, Long> {

	@Query("select s from ShortsLike s where s.shorts.id = :shortsId and "
		+ "s.shorts.isDeleted = false and s.user.id = :userId and s.user.deleted = false")
	Optional<ShortsLike> findByShortsIdAndUserIdAndIsDeletedFalse(@Param("shortsId") Long shortsId,
		@Param("userId") Long userId);

	@Modifying
	@Query("delete from ShortsLike sl where sl.shorts.id = :shortsId")
	void deleteByShortsId(@Param("shortsId") Long shortsId);
}
