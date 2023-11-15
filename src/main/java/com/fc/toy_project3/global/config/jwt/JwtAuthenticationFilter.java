package com.fc.toy_project3.global.config.jwt;

import com.fc.toy_project3.domain.member.exception.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 2. /api/members/signUp 주소에 대한 예외 처리
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.equals("/api/members/signUp") || requestURI.equals("/api/members/signIn") || requestURI.equals("/error")) {
            chain.doFilter(request, response);
            return;
        }
        // 2. validateToken으로 토큰 유효성 검사
        if (token != null) {
            String email = jwtTokenProvider.extractMemberIdFromToken(token);
            if (email != null) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가져와 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        throw new InvalidJwtException("토큰이 존재하지않습니다.");
    }
}