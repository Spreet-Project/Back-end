package com.team1.spreet.domain.feed.repository;

import com.team1.spreet.domain.feed.model.Feed;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    //최신순 feed 조회 시 page 별 조회
    @Query("select distinct f from Feed f inner join f.user where f.isDeleted = false order by f.createdAt DESC")
    Page<Feed> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    //feed 소프트 딜리트 메소드
    @Transactional
    @Modifying
    @Query("update Feed f set f.isDeleted = true where f.id = :feedId")
    void updateIsDeletedTrueById(@Param("feedId")Long feedId);

    @Transactional
    @Modifying
    @Query("update Feed f set f.title = :title, f.content = :content where f.id = :feedId and f.user.id = :userId")
    void updateFeedByIdAndUserId(@Param("title") String title, @Param("content") String content, @Param("feedId") Long feedId, @Param("userId") Long userId);

    @Query("select distinct f from Feed f join fetch f.user where f.id = :feedId")
    Optional<Feed> findByIdWithUser(Long feedId);

    List<Feed> findAllByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
}
