package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
	List<ShortsComment> findByShortsId(Long shortsId);
}
