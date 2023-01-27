package com.team1.spreet.repository;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    List<User> findByUserRoleAndIsDeletedFalse(UserRole userRole);

    Optional<User> findByLoginIdAndUserRoleAndIsDeletedFalse(String loginId, UserRole userRole);

    @Transactional
    @Modifying
    @Query("update User u set u.isDeleted = true where u.loginId = :loginId")
    void updateIsDeletedTrueByLoginId(@Param("loginId") String loginId);

    Optional<User> findByIdAndIsDeletedFalse(Long userId);
}
