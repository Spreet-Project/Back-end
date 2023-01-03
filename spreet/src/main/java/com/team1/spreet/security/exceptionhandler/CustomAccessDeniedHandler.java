package com.team1.spreet.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    private static final String CUSTOM_DEFAULT_ERROR_MSG = "권한이 없습니다.";

    private static final String DEFAULT_ERROR_MSG = "접근이 거부되었습니다.";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String errorMsg = accessDeniedException.getMessage().equals(DEFAULT_ERROR_MSG)
                ? CUSTOM_DEFAULT_ERROR_MSG
                : accessDeniedException.getMessage();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorMsg, HttpStatus.BAD_REQUEST.value());

        String result = objectMapper.writeValueAsString(errorResponseDto);

        response.getWriter().write(result);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
