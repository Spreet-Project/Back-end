package com.team1.spreet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatusCode {
    INVALID_FILE("잘못된 형식의 파일 입니다.", HttpStatus.BAD_REQUEST.value()),
    FAIL_FILE_UPLOAD("파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_EXIST_FEED("피드가 존재하지 않습니다", HttpStatus.NO_CONTENT.value());
    private final String msg;
    private final int statusCode;

    ErrorStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
