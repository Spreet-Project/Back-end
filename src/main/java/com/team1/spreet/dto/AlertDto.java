package com.team1.spreet.dto;

import com.team1.spreet.entity.Alert;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlertDto {
    @NoArgsConstructor
    @Getter
    public static class ResponseDto{
        private Long id;
        private String content;
        private String url;
        private boolean isRead;

        public ResponseDto(Alert alert) {
            this.id = alert.getId();
            this.content = alert.getContent();
            this.url = alert.getUrl();
            this.isRead = alert.isRead();
        }
    }
}
