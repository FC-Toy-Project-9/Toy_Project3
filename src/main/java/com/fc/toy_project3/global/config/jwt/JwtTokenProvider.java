package com.fc.toy_project3.global.config.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

@Configuration
public class JwtTokenProvider {
//    @Value("${jwt.secretKey}")
//    private String secretKey;
//    @Value("${jwt.tokenValidTime}")
//    private long tokenValidTime; // 30min
    private String secretKey = "toyproject9-3";
    private Long tokenValidTime = 10 * 60 * 1000L;
    @Autowired
    private UserDetailsService userDetailsService;

    public Authentication getAuthentication(String token) {
        String email = extractMemberIdFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 생성
    public String createJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractMemberIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            handleJwtException("JWT 토큰이 만료되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            handleJwtException("지원되지 않는 JWT 토큰입니다.", e);
        } catch (MalformedJwtException e) {
            handleJwtException("올바르게 구성되지 않은 JWT 토큰입니다.", e);
        } catch (SignatureException e) {
            handleJwtException("유효하지 않은 JWT 서명입니다.", e);
        } catch (IllegalArgumentException e) {
            handleJwtException("JWT 클레임이 비어있습니다.", e);
        }
        return null;
    }

    // 예외 처리를 담당하는 메서드
    private void handleJwtException(String message, Exception e) {
        // 예외 처리를 여기에 추가
        throw new JwtException(message, e);
    }
}
