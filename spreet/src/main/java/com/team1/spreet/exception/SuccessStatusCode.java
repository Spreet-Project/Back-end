package com.team1.spreet.exception;

import lombok.Getter;

@Getter
public enum SuccessStatusCode {

    EXAMPLE("이런식으로 적어주세요",200),

    SAVE_FEED("피드 작성 완료",200),
    UPDATE_FEED("피드 수정 완료",200),
    DELETE_FEED("피드 삭제 완료",200),
    GET_FEED("피드 조호 완료",200);

    private final String msg;
    private final int statusCode;

    SuccessStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
