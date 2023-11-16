package com.fc.toy_project3.domain.member.unit.service;

import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import com.fc.toy_project3.global.config.jwt.CustomUserDetailsService;
import com.fc.toy_project3.global.config.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private CustomUserDetailsService userDetailsService;

    void setUp() {
        MockitoAnnotations.openMocks(this); // 테스트 셋업
    }
    @Nested
    @DisplayName("signUp()은")
    class Context_signUp {

        @Test
        @DisplayName("회원가입할 수 있다.")
        void _willSuccess() {
            SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                    .email("test@mail.com")
                    .nickname("test")
                    .password("testTEST12!@")
                    .name("test")
                    .build();

            Member savedMember = Member.builder()
                    .id(1L)
                    .email(signUpRequestDTO.getEmail())
                    .nickname(signUpRequestDTO.getNickname())
                    .name(signUpRequestDTO.getName())
                    .password("encodedPassword")  // 가짜 암호화된 비밀번호
                    .build();

            when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
            when(memberRepository.findByNickname(any())).thenReturn(Optional.empty());
            when(memberRepository.save(any())).thenReturn(savedMember);

            SignUpResponseDTO signUpResponseDTO = memberService.signUp(signUpRequestDTO);

            assertEquals(1L, signUpResponseDTO.getMemberId());
            assertEquals("test@mail.com", signUpResponseDTO.getEmail());
            assertEquals("test", signUpResponseDTO.getNickname());
            assertEquals("test", signUpResponseDTO.getName());
        }
    }
    @Nested
    @DisplayName("signIn()은")
    class Context_signIn {

        @Test
        @DisplayName("로그인 할 수 있다.")
        void _willSuccess() {
            SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                    .email("test@mail.com")
                    .password("testTEST12!@")
                    .build();
            JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                    .jwtToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w")
                    .build();
            Member mockMember = Member.builder()
                    .id(1L)
                    .email(signInRequestDTO.getEmail())
                    .password("encodedPassword")
                    .build();
            // 권한 생성
            String responseToken = jwtResponseDTO.getJwtToken();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            UserDetails userDetails = new User("test@mail.com", "password", authorities);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "USER", userDetails.getAuthorities());
            String jwtToken = jwtTokenProvider.createJwtToken(authentication);
            assertNotNull(responseToken);
        }
        @Test
        @DisplayName("로그인 할 수 없다.")
        void signIn_InvalidCredentials_ThrowsException() {
            SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                    .email("test@mail.com")
                    .password("")
                    .build();
            assertThrows(UsernameNotFoundException.class, () -> memberService.signIn(signInRequestDTO));
        }
    }

    @Test
    void testJwtTokenCreation() {
        // given
        SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                .email("test@email.com")
                .password("testTEST12!@")
                .build();
        Member expectedMember = Member.builder()
                .id(1L)
                .email(signInRequestDTO.getEmail())
                .password("{bcrypt}$2a$10$CcHBLQHF0/wIZAy3.2ZHpe9XdUKUwqB8iAeG2M9Ip/Z12RMKUC.Oi")
                .build();


        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w"; // 예상되는 JWT 토큰 값

        // JwtTokenProvider 모의 객체 생성
        JwtTokenProvider jwtTokenProviderMock = mock(JwtTokenProvider.class);
        when(jwtTokenProviderMock.createJwtToken(any())).thenReturn(jwtToken); // createJwtToken이 호출될 때 예상되는 토큰 반환

        // 이전 코드에서 memberRepositoryMock가 누락된 부분을 추가합니다.
        MemberRepository memberRepositoryMock = mock(MemberRepository.class);

        // MemberService의 생성자에 MemberRepository 모의 객체를 포함시킵니다.
        MemberService memberService = new MemberService(memberRepositoryMock, passwordEncoder, jwtTokenProviderMock, userDetailsService);

        // when
        JwtResponseDTO jwtResponseDTO = memberService.signIn(signInRequestDTO);

        // then
        assertNotNull(jwtResponseDTO);
        assertEquals(jwtToken, jwtResponseDTO.getJwtToken());

        // JwtTokenProvider의 createJwtToken 메서드가 signIn 메서드에서 호출되었는지 확인
        verify(jwtTokenProviderMock, times(1)).createJwtToken(any());
    }
}
