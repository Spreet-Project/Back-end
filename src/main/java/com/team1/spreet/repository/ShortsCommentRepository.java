package com.team1.spreet.repository;

import com.team1.spreet.entity.ShortsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {
    List<ShortsComment> findByShortsId(Long shortsId);}
