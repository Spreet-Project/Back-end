package com.team1.spreet.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.dto.ErrorResponseDto;
import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            if (!jwtUtil.validateToken(token)) {
                jwtExceptionHandler(response, ErrorStatusCode.INVALID_JWT);
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);
            log.info(info.getSubject());
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String loginId) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        Authentication authentication = jwtUtil.getAuthentication(loginId);
        securityContext.setAuthentication(authentication);

        // >> 여기서 설정한 것을 @AuthenticationPrincipal 여기서 뽑아쓸 수 있음
        SecurityContextHolder.setContext(securityContext);
    }

    private void jwtExceptionHandler(HttpServletResponse response, ErrorStatusCode errorStatusCode) {
        response.setStatus(errorStatusCode.getStatusCode());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ErrorResponseDto(errorStatusCode.getMsg(), errorStatusCode.getStatusCode()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
