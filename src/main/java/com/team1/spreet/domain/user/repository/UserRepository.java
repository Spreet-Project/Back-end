package com.team1.spreet.domain.user.repository;

import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    List<User> findByUserRoleAndDeletedFalse(UserRole userRole);

    Optional<User> findByLoginIdAndUserRoleAndDeletedFalse(String loginId, UserRole userRole);

    Optional<User> findByIdAndDeletedFalse(Long userId);
}
