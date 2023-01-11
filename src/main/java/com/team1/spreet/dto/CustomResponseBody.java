package com.team1.spreet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.exception.SuccessStatusCode;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

@Getter
public class CustomResponseBody <T>{
    @ApiParam(value = "결과 메세지")
    private String msg;

    @ApiParam(value = "상태 코드")
    private int statusCode;

    @ApiParam(value = "반환 데이터")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    public CustomResponseBody(SuccessStatusCode commonStatusCode) {
        this.msg = commonStatusCode.getMsg();
        this.statusCode = commonStatusCode.getStatusCode();
    }

    public CustomResponseBody(SuccessStatusCode commonStatusCode, T data) {
        this.msg = commonStatusCode.getMsg();
        this.statusCode = commonStatusCode.getStatusCode();
        this.data = data;
    }
}
