package com.team1.spreet.exception;

import lombok.Getter;

@Getter
public enum ErrorStatusCode {
    EXAMPLE("이런식으로 적어주세요",400);

    private final String msg;
    private final int statusCode;

    ErrorStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
