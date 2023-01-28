package com.team1.spreet.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team1.spreet.global.common.model.SuccessStatusCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomResponseBody <T>{
    @ApiModelProperty(value = "결과 메세지")
    private String msg;

    @ApiModelProperty(value = "상태 코드")
    private int statusCode;

    @ApiModelProperty(value = "반환 데이터")
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
