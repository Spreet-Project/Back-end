package com.team1.spreet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.exception.SuccessStatusCode;

public class CustomResponseBody <T>{
    private String msg;
    private int statusCode;

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
