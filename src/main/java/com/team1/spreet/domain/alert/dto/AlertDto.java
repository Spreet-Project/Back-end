package com.team1.spreet.domain.alert.dto;

import com.team1.spreet.domain.alert.model.Alert;
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
