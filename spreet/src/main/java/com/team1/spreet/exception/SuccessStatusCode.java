package com.team1.spreet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessStatusCode {

    SAVE_FEED("피드 작성 완료",HttpStatus.OK.value()),
    UPDATE_FEED("피드 수정 완료",HttpStatus.OK.value()),
    DELETE_FEED("피드 삭제 완료",HttpStatus.OK.value()),
    GET_FEED("피드 조회 완료",HttpStatus.OK.value()),
    GET_RECENT_FEED("피드 최신순 조회 완료",HttpStatus.OK.value()),
    SIGNUP_SUCCESS("회원가입 성공", HttpStatus.OK.value()),
    LOGIN_SUCCESS("로그인 성공", HttpStatus.OK.value()),
    LIKE_FEED("피드 좋아요",HttpStatus.OK.value()),
    CANCEL_LIKE_FEED("피드 좋아요 취소",HttpStatus.OK.value()),

    SAVE_FEED_COMMENT("댓글 작성 완료",HttpStatus.OK.value()),
    UPDATE_FEED_COMMENT("댓글 수정 완료",HttpStatus.OK.value()),
    DELETE_FEED_COMMENT("댓글 삭제 완료",HttpStatus.OK.value());




    private final String msg;
    private final int statusCode;

    SuccessStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
