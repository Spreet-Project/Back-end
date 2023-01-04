package com.team1.spreet.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE shorts SET is_deleted = true WHERE shorts_id = ?")
@Where(clause = "is_deleted = false")
public class Shorts extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHORTS_ID")
    private Long id;            //쇼츠 primary key

    @Column(nullable = false)
    private String title;       //쇼츠 제목

    @Column(nullable = false)
    private String content;     //쇼츠 내용

    @Column(nullable = false)
    private String videoUrl;       //쇼츠 url

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)//쇼츠 카테고리
    private Category category;

    @Column(nullable = false)
    private boolean isDeleted = Boolean.FALSE;  //쇼츠 삭제 여부, 기본값=FALSE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;          //유저 단방향

    @OneToMany(mappedBy = "shorts", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShortsComment> shortsCommentList = new ArrayList<>();  //쇼츠 코맨트 양방향

    @OneToMany(mappedBy = "shorts", fetch = FetchType.LAZY, cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<ShortsLike> shortsLikeList = new ArrayList<>();        //쇼츠 좋아요 양방향
}
