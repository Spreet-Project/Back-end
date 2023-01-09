package com.team1.spreet.exception;

import lombok.Getter;

@Getter
public enum SuccessStatusCode {

    EXAMPLE("이런식으로 적어주세요",200);

    private final String msg;
    private final int statusCode;

    SuccessStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
