package com.team1.spreet.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.global.error.dto.ErrorResponseDto;
import com.team1.spreet.global.error.model.ErrorStatusCode;
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
                jwtExceptionHandler(response);
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

    private void jwtExceptionHandler(HttpServletResponse response) {
        response.setStatus(ErrorStatusCode.INVALID_JWT.getStatusCode());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ErrorResponseDto(
                ErrorStatusCode.INVALID_JWT.getMsg(), ErrorStatusCode.INVALID_JWT.getStatusCode()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
