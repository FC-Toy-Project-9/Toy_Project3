package com.fc.toy_project3.global.config.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetailsService;

import java.util.Date;

@Configuration
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.tokenValidTime}")
    private long tokenValidTime;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserDetailsService userDetailsService;
    public Authentication getAuthentication(String token) {
        String email = extractMemberIdFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()); // 인증 정보
    }

    // JWT 생성
    public String createJwtToken(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();  // 수정예정
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername()); //email

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
            throw new JwtException("JWT 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("올바르게 구성되지 않은 JWT 토큰입니다.");
        } catch (SignatureException e) {
            throw new JwtException("유효하지 않은 JWT 서명입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 클레임이 비어있습니다.");
        }
    }
}
