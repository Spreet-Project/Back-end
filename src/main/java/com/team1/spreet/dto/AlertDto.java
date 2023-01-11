package com.team1.spreet.dto;

import com.team1.spreet.entity.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationDto {
    @NoArgsConstructor
    @Getter
    public static class ResponseDto{
        private Long id;
        private String content;
        private String url;
        private boolean isRead;

        public ResponseDto(Notification notification) {
            this.id = notification.getId();
            this.content = notification.getContent();
            this.url = notification.getUrl();
            this.isRead = notification.isRead();
        }
    }
}
