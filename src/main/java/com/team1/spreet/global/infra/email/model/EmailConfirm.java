package com.team1.spreet.global.infra.email.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash(value = "EmailConfirm", timeToLive = 180)
@AllArgsConstructor
@Getter
@Setter
public class EmailConfirm {
    @Id
    @Indexed
    private String email;
    private String confirmNumber;
}
