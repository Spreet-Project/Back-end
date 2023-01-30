package com.team1.spreet.domain.alert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
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
    @Builder
    public Alert(String content, String url, boolean isRead, String sender, String receiver) {
        this.content = content;
        this.url = url;
        this.isRead = isRead;
        this.sender = sender;
        this.receiver = receiver;
    }
    public void ReadAlert(){
        this.isRead = true;
    }
}
