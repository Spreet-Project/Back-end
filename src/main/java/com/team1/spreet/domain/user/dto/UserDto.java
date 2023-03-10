package com.team1.spreet.domain.user.dto;

import com.team1.spreet.domain.user.model.Provider;
import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.domain.user.model.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UserDto {

    @Getter
    public static class SignupRequestDto {

        @Size(min = 5, max = 20, message = "아이디는 5~20자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        @ApiModelProperty(value = "로그인 ID", required = true)
        private String loginId;

        @Size(min = 2, max = 10, message = "닉네임은 2~10자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @ApiModelProperty(value = "닉네임", required = true)
        private String nickname;

        //6~15자, (알파벳, 숫자)가능, 특수문자 필수
        @Size(min = 6, max = 15, message = "비밀번호는 6~15자 이내의 길이로만 이루어질 수 있습니다.")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @ApiModelProperty(value = "비밀번호", required = true)
        private String password;

        //이메일 형식
        @Email
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @ApiModelProperty(value = "이메일", required = true)
        private String email;

        @ApiModelProperty(value = "이메일 인증 여부", required = true)
        @NotNull(message = "이메일 인증 확인 여부가 필요합니다.")
        private boolean emailConfirm;

        @ApiModelProperty(value = "프로필 이미지", required = true)
        private String profileImage;

        @ApiModelProperty(value = "회원 권한", required = true)
        private UserRole userRole;

        public User toEntity(String encodePassword, Provider provider) {
            return new User(this.loginId, this.nickname, this.password = encodePassword,
                this.email, this.profileImage, this.userRole, provider);
        }
    }

    @Getter
    public static class LoginRequestDto {
        @ApiModelProperty(value = "로그인 ID", required = true)
        private String loginId;

        @ApiModelProperty(value = "비밀번호", required = true)
        private String password;
    }

    @Getter
    public static class ResetPwRequestDto {
        @ApiModelProperty(value = "이메일", required = true)
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        private String email;

        @ApiModelProperty(value = "이메일 인증 여부", required = true)
        @NotNull(message = "이메일 인증 여부를 입력해주세요.")
        private boolean emailConfirm;

        @ApiModelProperty(value = "새로운 비밀번호", required = true)
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    public static class QuitRequestDto {
        @ApiModelProperty(value = "비밀번호", required = true)
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    public static class KakaoInfoDto {
        private Long id;
        private String nickname;
        private String email;

        private String profileImage;

        public KakaoInfoDto(Long id, String nickname, String email, String profileImage) {
            this.id = id;
            this.nickname = nickname;
            this.email = email;
            this.profileImage = profileImage;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class NaverInfoDto{
        private String id;
        private String nickname;
        private String email;
        private String profileImage;

        public NaverInfoDto(String id, String nickname, String email, String profileImage){
            this.id = id;
            this.nickname = nickname;
            this.email = email;
            this.profileImage = profileImage;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class LoginResponseDto{
        @ApiModelProperty(value = "닉네임")
        private String nickname;

        @ApiModelProperty(value = "회원 권한")
        private String userRole;

        public LoginResponseDto(String nickname, UserRole userRole) {
            this.nickname = nickname;
            this.userRole = userRole.value();
        }
    }

}