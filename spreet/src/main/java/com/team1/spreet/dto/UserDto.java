package com.team1.spreet.dto;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UserDto {

    @Getter
    public static class SignupRequestDto {

        //5자 이상 20자 이하, (알파벳(필수), 특수문자, 숫자)가능
        //2~10자, 알파벳 또는 한글 중 하나는 필수, 숫자 가능
        @Size(min = 5, max = 20, message = "아이디는 5~20자 이내의 길이로만 이루어질 수 있습니다.")
//        @Pattern(regexp = "^*[a-zA-Z][0-9][\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\\"]")
        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        private String loginId;

        @Size(min = 2, max = 10, message = "닉네임은 2~10자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")

        private String nickname;

        //6~15자, (알파벳, 숫자)가능, 특수문자 필수
        @Size(min = 6, max = 15, message = "비밀번호는 6~15자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;

        //이메일 형식
        @Email
        @NotNull(message = "이메일은 필수 입력 항목입니다.")
        private String email;

        private UserRole userRole;

        private boolean isDeleted = Boolean.FALSE;

        public User toEntity(String encodePassword) {
            return new User(this.loginId, this.nickname, this.password = encodePassword, this.email);
        }
    }

    @Getter
    public static class LoginRequestDto {
        private String loginId;
        private String password;
    }

    @Getter
    public static class KakaoInfoDto {
        private Long id;
        private String nickname;
        private String email;

        public KakaoInfoDto(Long id, String nickname, String email) {
            this.id = id;
            this.nickname = nickname;
            this.email = email;
        }
    }
}
