package com.team1.spreet.domain.shorts.repository;

import com.team1.spreet.domain.shorts.model.ShortsLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortsLikeRepository  extends JpaRepository<ShortsLike, Long>, ShortsLikeCustomRepository {

	Optional<ShortsLike> findByShortsIdAndUserId(Long shortsId, Long userId);

	void deleteByShortsId(Long shortsId);

	void deleteByUserId(Long userId);

}
