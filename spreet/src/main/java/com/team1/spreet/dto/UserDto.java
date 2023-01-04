package com.team1.spreet.dto;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import lombok.Getter;

@Getter
public class UserDto {

    @Getter
    public static class SignupRequestDto {
        private String loginId;
        private String username;
        private String nickname;
        private String password;
        private String email;
        private UserRole userRole;

        public User toEntity(String encodePassword) {
            return new User(this.loginId, this.username, this.nickname, this.password = encodePassword, this.email);
        }
    }

    @Getter
    public static class LoginRequestDto {
        private String loginId;
        private String password;
    }
}
