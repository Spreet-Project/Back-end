package com.team1.spreet.domain.user.repository;

import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.userRole in :userRole and u.deleted = false")
    List<User> findByUserRoleAndDeletedFalse(@Param("userRole")List<UserRole> userRole);

    Optional<User> findByLoginIdAndUserRoleAndDeletedFalse(String loginId, UserRole userRole);

    Optional<User> findByIdAndDeletedFalse(Long userId);
}
