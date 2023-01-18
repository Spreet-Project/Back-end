package com.team1.spreet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.spreet.jwt.JwtUtil;
import com.team1.spreet.security.exceptionhandler.CustomAccessDeniedHandler;
import com.team1.spreet.security.exceptionhandler.CustomAuthenticaionEntryPoint;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration  //Bean을 등록하기 위함이거나 설정파일을 만들기 위한 어노테이션
@RequiredArgsConstructor
@EnableWebSecurity  //스프링 Security 지원을 가능하게 함
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper om;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
            .antMatchers("/api/user/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/shorts/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/feed/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/event/**").permitAll()
            .antMatchers("/api/doc").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/v3/api-docs").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .anyRequest().authenticated();

        http
                .logout()
                .logoutUrl("/api/user/logout")
                .logoutSuccessUrl("/api/user/login")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.invalidate();
                })
                .logoutSuccessHandler(
                        ((request, response, authentication) -> response.sendRedirect("/api/user/login")))
                .deleteCookies("remember-me");

        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticaionEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler())

                .and()
                .headers()
                .frameOptions()
                .disable()

                .and()
                .cors()

                .and()
                .apply(new JwtConfig(jwtUtil, om));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000");

        config.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);

        config.addAllowedMethod("*");

        config.addAllowedHeader("*");

        config.setAllowCredentials(true);

        config.validateAllowCredentials();

        config.addExposedHeader("Access_Token");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler(om);
    }

    @Bean
    public CustomAuthenticaionEntryPoint customAuthenticaionEntryPoint() {
        return new CustomAuthenticaionEntryPoint(om);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
