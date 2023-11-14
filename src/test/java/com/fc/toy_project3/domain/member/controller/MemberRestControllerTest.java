package com.fc.toy_project3.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.config.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
        memberRepository.save(new Member((long) 1L, "ypd06021@naver.com", "asdfQWER1!", "fastcam", "패캠"));
    }


    @Test
    void testSignUpEndpoint() throws Exception {
        SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                .email("ypd06021@naver.com")
                .password("asdfQWER1!")
                .nickname("fastcam")
                .name("패캠")
                .build();

        SignUpResponseDTO signUpResponseDTO = SignUpResponseDTO.builder()
                .memberId(1L)
                .email("ypd06021@naver.com")
                .nickname("fastcam")
                .name("패캠")
                .build();

        when(memberService.signUp(any(SignUpRequestDTO.class))).thenReturn(signUpResponseDTO);

        mockMvc.perform(
                        post("/api/members/signUp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signUpRequestDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.email").value("ypd06021@naver.com"))
                .andExpect(jsonPath("$.data.name").value("패캠"))
                .andExpect(jsonPath("$.data.nickname").value("fastcam"))
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
    }


    @Test
    void testSignInEndpoint() throws Exception {
        SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                .email("ypd06021@naver.com")
                .password("asdfQWER1!")
                .build();

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .jwtToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w")
                .build();

        when(memberService.signIn(any(SignInRequestDTO.class))).thenReturn(jwtResponseDTO);
        mockMvc.perform(
                        post("/api/members/signIn")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signInRequestDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.jwtToken").value("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w"))
                .andExpect(jsonPath("$.message").value("로그인이 완료되었습니다."));
    }

}