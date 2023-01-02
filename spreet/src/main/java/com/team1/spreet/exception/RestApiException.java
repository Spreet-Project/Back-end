package com.team1.spreet.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{
    private String msg;
    private int statusCode;

    public RestApiException(ErrorStatusCode errorStatusCode) {
        this.msg = errorStatusCode.getMsg();
        this.statusCode = errorStatusCode.getStatusCode();
    }
}