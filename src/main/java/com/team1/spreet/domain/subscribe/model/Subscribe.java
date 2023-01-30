package com.team1.spreet.domain.subscribe.model;

import com.team1.spreet.domain.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID", nullable = false)
    private User publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSCRIBER_ID", nullable = false)
    private User subscriber;

    public Subscribe(User publisher, User subscriber) {
        this.publisher = publisher;
        this.subscriber = subscriber;
    }
}
