package com.team1.spreet.domain.subscribe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SubscribeDto {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestDto {

        private String nickname;
    }
}
