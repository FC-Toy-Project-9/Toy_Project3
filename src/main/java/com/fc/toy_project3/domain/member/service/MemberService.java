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


    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        String email = signUpRequestDTO.getEmail();
        String nickname = signUpRequestDTO.getNickname();

        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new ExistingMemberException("이미 존재하는 회원입니다.");
        });

        memberRepository.findByNickname(nickname).ifPresent(member -> {
            throw new ExistingMemberException("이미 존재하는 닉네임입니다.");
        });

        String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
        Member member = Member.builder()
                .email(email)
                .name(signUpRequestDTO.getName())
                .nickname(nickname)
                .password(encodedPassword)
                .build();

        Member savedMember = memberRepository.save(member);

        return SignUpResponseDTO.builder()
                .memberId(savedMember.getId())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .nickname(savedMember.getNickname())
                .build();
    }

    public JwtResponseDTO signIn (SignInRequestDTO signInRequestDTO) {
        String email = signInRequestDTO.getEmail();
        String password = signInRequestDTO.getPassword();

        // 비밀번호 검증
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지않습니다." + email));
        String dbPassword = member.getPassword();
        if (!passwordMatches(password, dbPassword)) {
            throw new ExistingMemberException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // 인증 후 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String jwtToken = jwtTokenProvider.createJwtToken(authentication);
        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .jwtToken(jwtToken)
                .build();

        return jwtResponseDTO;

    }
    private boolean passwordMatches(String inputPassword, String dbPassword) {
        return passwordEncoder.matches(inputPassword, dbPassword);
    }


    /**
     * 로그인 이후 유저 객체 리턴하는 메서드 : 현재 인증된 사용자의 정보를 확인하여 해당 사용자의 회원 정보를 반환
     *
     * @return 현재 사용자의 회원 정보가 담긴 SignUpResponseDTO 객체(nickname, email, name, memberId)
     */
     public SignUpResponseDTO userInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    /**
     * 주어진 memberId로 회원을 조회하여 반환합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 주어진 ID에 해당하는 회원 객체
     * @throws UsernameNotFoundException 주어진 ID에 해당하는 회원을 찾을 수 없을 때 발생하는 예외
     */
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(memberId+"해당 Id를 찾을 수 없습니다.")
                );
    }
}