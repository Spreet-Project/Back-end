package com.team1.spreet.repository;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

//    boolean existsByLoginIdAndIsDeleted(String loginId, boolean isDeleted);

//    @Query("select u from User u where u.userRole = :userRole and u.isCrew = false")
//    List<User> findByUserRoleAndCrewIsFalse(@Param(value = "userRole") UserRole userRole);

    List<User> findByUserRoleAndIsCrew(UserRole userRole, boolean isCrew);

    boolean existsByLoginIdAndUserRoleAndIsCrew(String loginId, UserRole userRole, boolean isCrew);
}
