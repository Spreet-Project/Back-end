package com.team1.spreet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    public FeedImage(String imageUrl, Feed feed) {
        this.imageUrl = imageUrl;
        this.feed = feed;
    }
}
