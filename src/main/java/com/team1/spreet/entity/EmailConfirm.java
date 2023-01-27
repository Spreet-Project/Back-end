package com.team1.spreet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("EmailConfirm")
@AllArgsConstructor
@ToString
@Getter
@Setter
public class EmailConfirm {
    @Id
    private String email;
    private String confirmNumber;
}
