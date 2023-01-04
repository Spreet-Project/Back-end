package com.team1.spreet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShortsLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHORTS_LIKE_ID")
    private Long id;

    @Column(nullable = false)
    private boolean isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHORTS_ID", nullable = false)
    private Shorts shorts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public ShortsLike(Shorts shorts, User user) {
        this.shorts = shorts;
        this.user = user;
        this.isLike = true;
    }

    public void shortsLike() {
        this.isLike = true;
    }

    public void shortsDisLike() {
        this.isLike = false;
    }
}
