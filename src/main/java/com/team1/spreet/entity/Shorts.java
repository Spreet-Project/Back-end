package com.team1.spreet.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private Long likeCount = 0L;

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

    public Shorts(String title, String content, String videoUrl, Category category, User user) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.category = category;
        this.user = user;
    }

    public void update(String title, String content, String videoUrl, Category category, User user) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.category = category;
        this.user = user;
    }

    public void addLike() {
        this.likeCount++;
    }

    public void cancelLike() {
        this.likeCount--;
    }
}
