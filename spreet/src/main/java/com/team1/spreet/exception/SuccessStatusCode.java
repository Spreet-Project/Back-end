package com.team1.spreet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessStatusCode {

    SIGNUP_SUCCESS("회원가입 성공", HttpStatus.OK.value()),
    LOGIN_SUCCESS("로그인 성공", HttpStatus.OK.value()),
    SAVE_SHORTS("Shorts 등록 성공", HttpStatus.OK.value()),
    UPDATE_SHORTS("Shorts 수정 성공", HttpStatus.OK.value()),
    DELETE_SHORTS("Shorts 삭제 성공", HttpStatus.OK.value()),
    GET_SHORTS("Shorts 조회 성공", HttpStatus.OK.value()),
    GET_SHORTS_BY_CATEGORY("카테고리별 Shorts 조회 성공", HttpStatus.OK.value()),
    SHORTS_LIKE("Shorts 좋아요 성공", HttpStatus.OK.value()),
    SHORTS_DISLIKE("Shorts 좋아요 취소 성공", HttpStatus.OK.value()),
    SAVE_SHORTS_COMMENT("Shorts 댓글 등록 성공", HttpStatus.OK.value()),
    UPDATE_SHORTS_COMMENT("Shorts 댓글 수정 성공", HttpStatus.OK.value()),
    DELETE_SHORTS_COMMENT("Shorts 댓글 삭제 성공", HttpStatus.OK.value());

    private final String msg;
    private final int statusCode;

    SuccessStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
