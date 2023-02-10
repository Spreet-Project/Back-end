package com.team1.spreet.domain.alarm.dto;

import com.team1.spreet.domain.alarm.model.Alarm;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlarmDto {
    @NoArgsConstructor
    @Getter
    public static class ResponseDto{
        private Long id;
        private String content;
        private String url;
        private boolean read;

        public ResponseDto(Alarm alarm) {
            this.id = alarm.getId();
            this.content = alarm.getContent();
            this.url = alarm.getUrl();
            this.read = alarm.isChecked();
        }
    }
}
