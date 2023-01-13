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
    GET_SHORTS_BY_ALL_CATEGORY("모든 카테고리 Shorts 조회 성공", HttpStatus.OK.value()),
    SHORTS_LIKE("Shorts 좋아요 성공", HttpStatus.OK.value()),
    SHORTS_DISLIKE("Shorts 좋아요 취소 성공", HttpStatus.OK.value()),
    SAVE_SHORTS_COMMENT("Shorts 댓글 등록 성공", HttpStatus.OK.value()),
    UPDATE_SHORTS_COMMENT("Shorts 댓글 수정 성공", HttpStatus.OK.value()),
    DELETE_SHORTS_COMMENT("Shorts 댓글 삭제 성공", HttpStatus.OK.value()),
    GET_SHORTS_COMMENTS("Shorts 댓글 리스트 조회 성공", HttpStatus.OK.value()),
    SAVE_FEED("피드 작성 완료",HttpStatus.OK.value()),
    UPDATE_FEED("피드 수정 완료",HttpStatus.OK.value()),
    DELETE_FEED("피드 삭제 완료",HttpStatus.OK.value()),
    GET_FEED("피드 조회 완료",HttpStatus.OK.value()),
    GET_RECENT_FEED("피드 최신순 조회 완료",HttpStatus.OK.value()),
    LIKE_FEED("피드 좋아요",HttpStatus.OK.value()),
    CANCEL_LIKE_FEED("피드 좋아요 취소",HttpStatus.OK.value()),
    SAVE_FEED_COMMENT("댓글 작성 완료",HttpStatus.OK.value()),
    UPDATE_FEED_COMMENT("댓글 수정 완료",HttpStatus.OK.value()),
    DELETE_FEED_COMMENT("댓글 삭제 완료",HttpStatus.OK.value()),
    GET_CREW_LIST("크루 회원 승인 대기 리스트 조회 성공", HttpStatus.OK.value()),
    APPROVE_ROLE_CREW("크루 회원 승인 성공", HttpStatus.OK.value()),
    REJECT_ROLE_CREW("크루 회원 거절 성공", HttpStatus.OK.value()),
    EMAIL_SEND_SUCCESS("이메일 전송 성공", HttpStatus.OK.value()),
    EMAIL_CONFIRM_SUCCESS("이메일 인증 성공", HttpStatus.OK.value()),
    ID_DUPLICATE_CHECK("아이디 중복 확인 성공. 사용 가능한 아이디입니다.", HttpStatus.OK.value()),
    NICKNAME_DUPLICATE_CHECK("닉네임 중복 확인 성공. 사용 가능한 닉네임입니다.", HttpStatus.OK.value());

    private final String msg;
    private final int statusCode;

    SuccessStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
