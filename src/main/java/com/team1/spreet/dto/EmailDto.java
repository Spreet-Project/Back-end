package com.team1.spreet.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class EmailDto {
    @ApiModelProperty(value = "이메일", required = true)
    private String email;
    @ApiModelProperty(value = "확인 코드", required = true)
    private String confirmCode;
}
