package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsLikeRepository  extends JpaRepository<ShortsLike, Long> {

	Optional<ShortsLike> findByShortsIdAndUserId(Long shortsId, Long userId);
}
