package com.team1.spreet.global.infra.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmailDto {
    private String email;
    private String confirmCode;
}
