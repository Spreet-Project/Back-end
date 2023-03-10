package com.team1.spreet.domain.feed.model;

import com.team1.spreet.domain.user.model.User;
import com.team1.spreet.global.common.model.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
public class Feed extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEED_ID")
    private Long id;            //피드 primary key

    @Column(nullable = false)
    private String title;       //피드 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;     //피드 내용

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;  //피드 삭제 여부, 기본값=FALSE

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
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void isDeleted(){
        deleted = true;
    }
    public void addLike() {
        this.likeCount++;
    }

    public void cancelLike() {
        this.likeCount--;
    }
}
