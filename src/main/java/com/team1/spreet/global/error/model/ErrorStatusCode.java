package com.team1.spreet.global.error.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatusCode {
    INVALID_JWT("유효하지 않는 JWT 입니다.", HttpStatus.BAD_REQUEST.value()),
    OVERLAPPED_ID("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST.value()),
    OVERLAPPED_NICKNAME("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST.value()),
    DELETED_ACCOUNT("최근에 회원탈퇴한 계정입니다.", HttpStatus.BAD_REQUEST.value()),
    PASSWORD_CONFIRM_INCORRECT("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_FILE("잘못된 형식의 파일 입니다.", HttpStatus.BAD_REQUEST.value()),
    FAIL_FILE_UPLOAD("파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_EXIST_SHORTS("존재하지 않는 쇼츠입니다.", HttpStatus.BAD_REQUEST.value()),
    UNAVAILABLE_MODIFICATION("작성자만 수정/삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_FEED("존재하지 않는 피드입니다", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_COMMENT("존재하지 않는 댓글입니다",HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_ALARM("존재하지 않는 알람입니다",HttpStatus.BAD_REQUEST.value()),
    WAITING_CREW_APPROVAL("크루회원 승인 대기 중입니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_USER("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST.value()),
    EMAIL_CONFIRM_INCORRECT("올바른 인증코드가 아닙니다.", HttpStatus.BAD_REQUEST.value()),
    EMAIL_CONFIRM_NULL_EXCEPTION("입력하신 email로 db에서 값을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    OVERLAPPED_EMAIL("중복된 이메일입니다.", HttpStatus.BAD_REQUEST.value()),
    EMAIL_CONFIRM_EXCEPTION("이메일 인증이 되지 않았습니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_EXIST_EVENT("존재하지 않는 행사 게시글 입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_PASSWORD("기존과 동일한 비밀번호는 설정하실 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    MISMATCH_EMAIL("등록된 이메일과 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());

    private final String msg;
    private final int statusCode;

    ErrorStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
