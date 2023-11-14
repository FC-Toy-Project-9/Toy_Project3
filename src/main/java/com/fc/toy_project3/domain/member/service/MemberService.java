package com.fc.toy_project3.domain.member.service;

import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.exception.ExistingMemberException;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.global.config.jwt.CustomUserDetailsService;
import io.jsonwebtoken.Jwt;
import com.fc.toy_project3.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;


    // 회원가입
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        // 데이터베이스에서 확인
        String email = signUpRequestDTO.getEmail();
        String nickname = signUpRequestDTO.getNickname();

        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new ExistingMemberException("이미 존재하는 회원입니다.");
        });

        memberRepository.findByNickname(nickname).ifPresent(member -> {
            throw new ExistingMemberException("이미 존재하는 닉네임입니다.");
        });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());

        Member member = Member.builder()
                .email(email)
                .name(signUpRequestDTO.getName())
                .nickname(nickname)
                .password(encodedPassword)
                .build();

        return memberRepository.save(member).toMemberResponseDTO();
    }

    public JwtResponseDTO signIn (SignInRequestDTO signInRequestDTO) {
        String email = signInRequestDTO.getEmail();
        String password = signInRequestDTO.getPassword();

        // 사용자 검증
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null || !passwordMatches(userDetails.getPassword(), password)) {
            throw new ExistingMemberException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 인증 후 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String jwtToken = jwtTokenProvider.createJwtToken(authentication);
        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .jwtToken(jwtToken)
                .build();

        System.out.println("Generated Token: " + jwtToken);
        return jwtResponseDTO;

    }
    private boolean passwordMatches(String hashedPassword, String inputPassword) {
        return passwordEncoder.matches(inputPassword, hashedPassword);
    }

    public SignUpResponseDTO test() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        // 현재 사용자의 정보 확인
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Member member = memberRepository.findByEmail(email).get();
            // 필요한 작업 수행
            SignUpResponseDTO testResponseDTO = SignUpResponseDTO.builder()
                    .nickname(member.getNickname())
                    .email(email)
                    .name(member.getName())
                    .memberId(member.getId())
                    .build();
            return testResponseDTO;
        }
        return null;
    }

}