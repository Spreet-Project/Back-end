package com.team1.spreet.dto;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserDto {

    @Getter
    public static class SignupRequestDto {

        //5자 이상 20자 이하, (알파벳(필수), 특수문자, 숫자)가능
        //2~10자, 알파벳 또는 한글 중 하나는 필수, 숫자 가능

        @Size(min = 5, max = 20, message = "아이디는 5~20자 이내의 길이로만 이루어질 수 있습니다.")
//        @Pattern(regexp = "^*[a-zA-Z][0-9][\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\\"]")
        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        @ApiParam(value = "로그인 ID", required = true)
        private String loginId;

        @Size(min = 2, max = 10, message = "닉네임은 2~10자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @ApiParam(value = "닉네임", required = true)
        private String nickname;

        //6~15자, (알파벳, 숫자)가능, 특수문자 필수
        @Size(min = 6, max = 15, message = "비밀번호는 6~15자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @ApiParam(value = "비밀번호", required = true)
        private String password;

        //이메일 형식
        @Email
        @NotNull(message = "이메일은 필수 입력 항목입니다.")
        @ApiParam(value = "이메일", required = true)
        private String email;

        @ApiParam(value = "회원 구분")
        private UserRole userRole;

        public User toEntity(String encodePassword) {
            return new User(this.loginId, this.nickname, this.password = encodePassword, this.email, this.userRole);
        }
    }

    @Getter
    public static class LoginRequestDto {
        @ApiParam(value = "로그인 ID", required = true)
        private String loginId;

        @ApiParam(value = "비밀번호", required = true)
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

    @NoArgsConstructor
    @Getter
    public static class CrewResponseDto {
        @ApiParam(value = "로그인 ID")
        private String loginId;

        @ApiParam(value = "닉네임")
        private String nickname;

        @ApiParam(value = "크루회원 여부")
        private boolean isCrew;

        public CrewResponseDto(User user) {
            this.loginId = user.getLoginId();
            this.nickname = user.getNickname();
            this.isCrew = user.isCrew();
        }
    }
}
