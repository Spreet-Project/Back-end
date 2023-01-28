package com.team1.spreet.global.error.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponseDto {
    @ApiModelProperty(value = "에러 메세지")
    private String msg;
    @ApiModelProperty(value = "에러 코드")
    private int statusCode;

    public ErrorResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
