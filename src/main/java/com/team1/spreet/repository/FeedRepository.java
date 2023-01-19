package com.team1.spreet.repository;

import com.team1.spreet.entity.Feed;
import com.team1.spreet.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    //최신순 feed 조회 시 page 별 조회
    @Query("select f from Feed f where f.isDeleted = false order by f.createdAt DESC")
    Page<Feed> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    //feed 소프트 딜리트 메소드
    @Transactional
    @Modifying
    @Query("update Feed f set f.isDeleted = true where f.id = :feedId")
    void updateIsDeletedTrueById(@Param("feedId")Long feedId);
    //특정 유저가 작성한 feed 찾기
    Optional<Feed> findByIdAndUserIdAndIsDeletedFalse(Long feedId, Long userId);

    @Transactional
    @Modifying
    @Query("update Feed f set f.title = ?1, f.content = ?2 where f.id = ?3 and f.user = ?4")
    void updateTitleAndContentByIdAndUser(String title, String content, Long id, User user);

}
