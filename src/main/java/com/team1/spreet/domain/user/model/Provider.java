package com.team1.spreet.domain.user.model;

import lombok.Getter;

@Getter
public enum Provider {
    LOCAL("LOCAL"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String type;

    Provider(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }
}
