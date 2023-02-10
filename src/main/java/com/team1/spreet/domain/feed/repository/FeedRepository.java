package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedCustomRepository {

}
