package com.team1.spreet.dto;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponseDto {
    @ApiParam(value = "에러 메세지")
    private String msg;
    @ApiParam(value = "에러 코드")
    private int statusCode;

    public ErrorResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
