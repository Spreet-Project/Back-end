package com.team1.spreet.domain.user.model;

import com.team1.spreet.global.common.model.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "USERS")
@DynamicUpdate
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false ,name = "USER_ID")
    private Long id;            //유저 테이블 primary key

    @Column(nullable = false, unique = true)
    private String loginId;     //로그인 아이디

    @Column(unique = true)
    private String nickname;    //유저 닉네임

    @Column(nullable = false)
    private String password;    //유저 비밀번호

    @Column(nullable = false, unique = true)
    private String email;       //유저 이메일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;  //유저 권한

    @Column(nullable = false)
    private boolean deleted;  //유저 탈퇴 여부, 기본값=FALSE

    @Column(nullable = false)
    private String profileImage;   //프로필 사진

    public User(String loginId, String nickname, String password, String email, String profileImage, UserRole userRole) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.deleted = Boolean.FALSE;
        this.profileImage = profileImage;
    }

    public User socialIdUpdate(String socialId) {
        this.loginId = socialId;
        return this;
    }

    public void approveCrew() {
        this.userRole = UserRole.ROLE_ACCEPTED_CREW;
    }

    public void rejectCrew() {
        this.userRole = UserRole.ROLE_USER;
    }

    //프로필 이미지 디폴트값
    @PrePersist
    public void prePersist(){
        this.profileImage =
                this.profileImage == null ||
                this.profileImage.equals("https://ssl.pstatic.net/static/pwe/address/img_profile.png") ||
                this.profileImage.equals("http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg")
                ? "https://spreet-bucket.s3.ap-northeast-2.amazonaws.com/spreet+%E1%84%85%E1%85%A9%E1%84%80%E1%85%A92.png"
                : this.profileImage;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void deleteUser() {
        this.deleted = true;
    }
}