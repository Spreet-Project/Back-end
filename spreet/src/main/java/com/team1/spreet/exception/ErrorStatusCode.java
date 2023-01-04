package com.team1.spreet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatusCode {
    NULL_TOKEN_EXCEPTION("헤더에 토큰이 없습니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_JWT_SIGNATURE_EXCEPTION("유효하지 않는 JWT 서명입니다.", HttpStatus.BAD_REQUEST.value()),
    EXPIRED_JWT_TOKEN_EXCEPTION("만료된 JWT token 입니다.", HttpStatus.BAD_REQUEST.value()),
    UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰 입니다.", HttpStatus.BAD_REQUEST.value()),
    TOKEN_ILLEGAL_ARGUMENT_EXCEPTION("잘못된 JWT 토큰입니다.", HttpStatus.BAD_REQUEST.value()),
    NULL_USER_ID_DATA_EXCEPTION("입력하신 아이디는 없는 아이디입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_FILE("잘못된 형식의 파일 입니다.", HttpStatus.BAD_REQUEST.value()),
    FAIL_FILE_UPLOAD("파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_FOUND_SHORTS("존재하지 않는 Shorts 입니다.", HttpStatus.BAD_REQUEST.value()),
    UNAVAILABLE_MODIFICATION("작성자만 수정/삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_FOUND_SHORTS_COMMENT("존재하지 않는 Shorts 댓글 입니다.", HttpStatus.BAD_REQUEST.value());

    private final String msg;
    private final int statusCode;

    ErrorStatusCode(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
