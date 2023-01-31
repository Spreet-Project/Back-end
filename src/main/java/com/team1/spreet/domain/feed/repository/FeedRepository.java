package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    //최신순 feed 조회 시 page 별 조회
    @Query("select distinct f from Feed f inner join f.user where f.deleted = false order by f.createdAt DESC")
    Page<Feed> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    @Query("select distinct f from Feed f join fetch f.user where f.id = :feedId")
    Optional<Feed> findByIdWithUser(Long feedId);

    List<Feed> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);
}
