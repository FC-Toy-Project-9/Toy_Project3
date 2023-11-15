package com.fc.toy_project3.domain.member.service;

import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.exception.ExistingMemberException;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import com.fc.toy_project3.global.config.jwt.CustomUserDetailsService;
import com.fc.toy_project3.global.config.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private MemberService memberService;

    @Test
    void signUp_Success() {

        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("ypd06021@naver.com", "패캠", "fastcam", "asdfQWER1!");
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(memberRepository.findByNickname(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(memberRepository.save(any())).thenReturn(new Member(1L, "ypd06021@naver.com", "encodedPassword", "패캠", "fastcam"));

        SignUpResponseDTO response = memberService.signUp(signUpRequestDTO);

        assertNotNull(response);
        assertEquals("fastcam", response.getNickname());
        assertEquals("ypd06021@naver.com", response.getEmail());
        assertEquals("패캠", response.getName());
        assertEquals(1L, response.getMemberId());
        verify(memberRepository, times(1)).save(any());
    }

    @Test
    void signUp_Failure_DuplicateEmail() {

        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("ypd06021@naver.com", "패캠", "fastcam", "asdfQWER1!");
        when(memberRepository.findByEmail("ypd06021@naver.com")).thenReturn(
                Optional.of(Member.builder()
                        .id(1L)
                        .email("ypd06021@naver.com")
                        .name("패캠")
                        .nickname("fastcam")
                        .password("encodedPassword")
                        .build())
        );

        assertThrows(ExistingMemberException.class, () -> memberService.signUp(signUpRequestDTO));
        verify(memberRepository, never()).save(any());
    }

    @Test
    void signIn_Success() {

        SignInRequestDTO signInRequestDTO = new SignInRequestDTO("ypd06021@naver.com", "{bcrypt}$2a$10$zQcxtnPtIP05tA2q05AVeeVmD5akTm3HyH0KyZVtldjKmkIRB.tu2");

        String email = signInRequestDTO.getEmail();
        String password = signInRequestDTO.getPassword();
        Member member = Member.builder()
                .id(1L)
                .email("ypd06021@naver.com")
                .name("패캠")
                .nickname("fastcam")
                .password("{bcrypt}$2a$10$zQcxtnPtIP05tA2q05AVeeVmD5akTm3HyH0KyZVtldjKmkIRB.tu2")
                .build();
        // memberRepository.findByEmail(email)에 대한 목(Mock) 설정
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member)); // 존재하는 이메일에 대한 Member 객체 반환 설정


        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "fastcam", "ypd06021@naver.com","패캠");
        when(userDetailsService.loadUserByUsername(email)).thenReturn(customUserDetails);
        when(passwordEncoder.matches("{bcrypt}$2a$10$zQcxtnPtIP05tA2q05AVeeVmD5akTm3HyH0KyZVtldjKmkIRB.tu2", "{bcrypt}$2a$10$zQcxtnPtIP05tA2q05AVeeVmD5akTm3HyH0KyZVtldjKmkIRB.tu2")).thenReturn(true);
        when(jwtTokenProvider.createJwtToken(any())).thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w");

        JwtResponseDTO response = memberService.signIn(signInRequestDTO);

        assertNotNull(response);
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w", response.getJwtToken());
        verify(jwtTokenProvider, times(1)).createJwtToken(any());
    }
    @Test
    void signIn_Failure_InvalidCredentials() {

        SignInRequestDTO signInRequestDTO = new SignInRequestDTO("nonexisting@email.com", "wrongPassword");
        when(userDetailsService.loadUserByUsername(any()))
                .thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> memberService.signIn(signInRequestDTO));
        verify(jwtTokenProvider, never()).createJwtToken(any());
    }

    @Test
    @WithMockUser(username = "ypd06021@naver.com", password = "{bcrypt}$2a$10$AXubVWjicX7CXOu94iA2j.yHFq1QmBb0vvIKszO/Hik7P38hEp8KK")
    void test_Success_AuthenticatedUser() {

        Member member = new Member(1L, "ypd06021@naver.com", "bcrypt}$2a$10$AXubVWjicX7CXOu94iA2j.yHFq1QmBb0vvIKszO/Hik7P38hEp8KK", "패캠", "fastcam");
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        SignUpResponseDTO response = memberService.userInfo();

        assertNotNull(response);
        assertEquals("ypd06021@naver.com", response.getEmail());
        assertEquals("패캠", response.getName());
        assertEquals("fastcam", response.getNickname());
        assertEquals(1L, response.getMemberId());
    }

    @Test
    void test_Success_UnauthenticatedUser() {

        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

        SignUpResponseDTO response = memberService.userInfo();

        assertNull(response);
    }

}