package com.team1.spreet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column
    private String content;    //알림 내용

    @Column
    private String url;    //알림 클릭시 이동할 페이지

    @Column
    private boolean isRead;    //알림 확인 여부

    @Column
    private String sender;

    @Column
    private String receiver;

}
