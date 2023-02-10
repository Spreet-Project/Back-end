package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long>, FeedCommentCustomRepository {

}
