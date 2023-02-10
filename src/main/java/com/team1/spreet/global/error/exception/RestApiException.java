package com.team1.spreet.global.error.exception;

import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.common.model.SuccessStatusCode;
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

    public RestApiException(SuccessStatusCode successStatusCode) {
        this.msg = successStatusCode.getMsg();
        this.statusCode = successStatusCode.getStatusCode();
    }
}