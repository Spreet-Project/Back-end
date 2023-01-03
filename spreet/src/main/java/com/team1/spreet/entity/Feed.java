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
@SQLDelete(sql = "UPDATE feed SET is_deleted = true WHERE feed_id = ?")
@Where(clause = "is_deleted = false")
public class Feed extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEED_ID")
    private Long id;            //피드 primary key

    @Column(nullable = false)
    private String title;       //피드 제목

    @Column(nullable = false)
    private String content;     //피드 내용

    /*@Column(nullable = false)
    private List<String> imageList;       //피드 이미지 url*/

    @Column(nullable = false)
    private boolean isDeleted = Boolean.FALSE;  //피드 삭제 여부, 기본값=FALSE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;          //유저 primary key

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY, cascade = CascadeType.ALL
    , orphanRemoval = true)
    private List<FeedComment> feedCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY, cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<FeedLike> feedLikeList = new ArrayList<>();

    public Feed(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
    public void update(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
    public void setDeleted(){
        this.isDeleted = true;
    }
}
