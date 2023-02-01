package com.team1.spreet.domain.alarm.model;

import com.team1.spreet.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column
    private String content;    //알림 내용

    @Column
    private String url;    //알림 클릭시 이동할 페이지

    @Column
    private boolean read;    //알림 확인 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID")
    private User receiver;
    @Builder
    public Alarm(String content, String url, boolean read, User sender, User receiver) {
        this.content = content;
        this.url = url;
        this.read = read;
        this.sender = sender;
        this.receiver = receiver;
    }
    public void readAlarm(){
        this.read = true;
    }
}
