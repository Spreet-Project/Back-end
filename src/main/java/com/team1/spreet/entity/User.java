package com.team1.spreet.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "USERS")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE user_id = ?")
@Where(clause = "is_deleted = false")
public class User extends TimeStamped{

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

    @Column(nullable = false)
    private String email;       //유저 이메일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;  //유저 역할

    @Column(nullable = false)
    private boolean isDeleted;  //유저 탈퇴 여부, 기본값=FALSE

    @Column(nullable = false)
    private boolean isCrew;    //크루회원 여부

    @Column(nullable = true)       // 카카오 로그인에서 프로필 사진 구현이 끝나면 nullable = false 로 변경
    private String profileImage;   //프로필 사진

    public User(String loginId, String nickname, String password, String email, UserRole userRole) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.isDeleted = Boolean.FALSE;
        this.isCrew = Boolean.FALSE;
    }

    /*
      프로필 사진 추가, 네이버 로그인 구현으로 인해 이 생성자는 불필요.
      카카오 로그인 코드 변경 후 삭제 필요!
     */
    public User(Long kakaoId, String nickname, String password, String email) {
        this.loginId = kakaoId.toString();
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userRole = UserRole.ROLE_USER;
        this.isDeleted = Boolean.FALSE;
        this.isCrew = Boolean.FALSE;
    }

    public User(Long socialId, String nickname, String password, String email, String profileImage) {
        this.loginId = socialId.toString();
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userRole = UserRole.ROLE_USER;
        this.isDeleted = Boolean.FALSE;
        this.isCrew = Boolean.FALSE;
        this.profileImage = profileImage;
    }

    public User socialIdUpdate(Long socialId) {
        this.loginId = socialId.toString();
        return this;
    }

    public void approveCrew() {
        this.isCrew = true;
    }

    public void rejectCrew() {
        this.userRole = UserRole.ROLE_USER;
    }
}