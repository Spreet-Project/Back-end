package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsLikeRepository  extends JpaRepository<ShortsLike, Long> {

	@Query("select s from ShortsLike s where s.shorts.id = :shortsId and "
		+ "s.shorts.isDeleted = false and s.user.id = :userId and s.user.isDeleted = false")
	Optional<ShortsLike> findByShortsIdAndUserIdAndIsDeletedFalse(@Param("shortsId") Long shortsId,
		@Param("userId") Long userId);

	@Modifying
	@Query("delete from ShortsLike sl where sl.shorts.id = :shortsId")
	void deleteByShortsId(@Param("shortsId") Long shortsId);
}
