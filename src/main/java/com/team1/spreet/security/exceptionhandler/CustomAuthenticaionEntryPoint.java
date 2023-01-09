package com.team1.spreet.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticaionEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private static final String DEFAULT_ERROR_MSG = "Full acthentication is required to access this resource";
    private static final String CUSTOM_DEFAULT_ERROR_MSG = "로그인이 필요합니다.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String errorMsg = authException.getMessage();

        if (errorMsg.equals(DEFAULT_ERROR_MSG)) {
            errorMsg = CUSTOM_DEFAULT_ERROR_MSG;
        }

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorMsg, HttpStatus.BAD_REQUEST.value());

        String result = objectMapper.writeValueAsString(errorResponseDto);

        response.getWriter().write(result);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
