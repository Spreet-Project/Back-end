package com.team1.spreet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.jwt.JwtUtil;
import com.team1.spreet.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, objectMapper);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
