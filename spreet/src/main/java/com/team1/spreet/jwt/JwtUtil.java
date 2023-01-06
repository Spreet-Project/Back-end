package com.team1.spreet.jwt;

import com.team1.spreet.exception.ErrorStatusCode;
import com.team1.spreet.exception.RestApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    // Header KEY값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    //사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    //Token 식별자
    private static final String BEARER_PREFIX = "Bearer ";
    //토큰 만료 시간
    private static final long TOKEN_TIME = 15 * 60 * 1000L;

    private final Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtUtil(@Value("${jwt.security.key}") String securityKey) {
        byte[] keyBytes = Decoders.BASE64.decode(securityKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //Header에서 Token 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //JWT 토큰 생성
    public String createToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(authentication.getName())
                        .claim(AUTHORIZATION_KEY, authorities)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = getUserInfoFromToken(token);

        if(claims.get(AUTHORIZATION_KEY) == null) throw new InvalidCookieException("로그인 정보가 잘못되었습니다. 다시 로그인해주세요.");

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //JWT 토큰 검정
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new RestApiException(ErrorStatusCode.INVALID_JWT_SIGNATURE_EXCEPTION);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
            throw new RestApiException(ErrorStatusCode.EXPIRED_JWT_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new RestApiException(ErrorStatusCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new RestApiException(ErrorStatusCode.TOKEN_ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    //토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
