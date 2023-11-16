package com.fc.toy_project3.domain.member.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.member.controller.MemberRestController;
import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Member REST Controller Test
 */
@WebMvcTest(MemberRestController.class)
class MemberRestControllerTest {

    @InjectMocks
    private MemberRestController memberRestController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;
    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    private CustomUserDetails makeUserInfo() {
        return new CustomUserDetails(1L, "test", "test@mail.com", "test");
    }

    @Nested
    @DisplayName("회원가입을 ")
    class signUp {

        @Test
        @DisplayName("할 수 있다.")
        @WithAnonymousUser
        void _willSuccess() throws Exception {
            SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                    .email("test@mail.com")
                    .nickname("test")
                    .password("testTEST12!@")
                    .name("test")
                    .build();
            SignUpResponseDTO signUpResponseDTO = SignUpResponseDTO.builder()
                    .memberId(1L)
                    .email("test@mail.com")
                    .nickname("test")
                    .name("test")
                    .build();

            when(memberService.signUp(any(SignUpRequestDTO.class))).thenReturn(signUpResponseDTO);

            mockMvc.perform(post("/api/members/signup")
                            .with(user(makeUserInfo()))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(signUpRequestDTO)
                            ))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                    .andExpect(jsonPath("$.data.memberId").value(1L))
                    .andExpect(jsonPath("$.data.email").value("test@mail.com"))
                    .andExpect(jsonPath("$.data.name").value("test"))
                    .andExpect(jsonPath("$.data.nickname").value("test"))
                    .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("할 수 없다.")
        void _willFail() throws Exception {
            SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                    .email("")
                    .password("test")
                    .nickname("test")
                    .name("test")
                    .build();
            mockMvc.perform(
                            post("/api/members/signup")
                                    .with(user(makeUserInfo()))
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(signUpRequestDTO)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
        }

    }


    @Nested
    @DisplayName("로그인을 ")
    class signIn {
        @Test
        @DisplayName("할 수 있다.")
        void _willSuccess() throws Exception {
            SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                    .email("test@mail.com")
                    .password("testTEST12!@")
                    .build();
            JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                    .jwtToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w")
                    .build();
            when(memberService.signIn(any(SignInRequestDTO.class))).thenReturn(jwtResponseDTO);

            mockMvc.perform(
                            post("/api/members/signin")
                                    .with(user(makeUserInfo()))
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(signInRequestDTO))
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.data.jwtToken").value("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w"))
                    .andExpect(jsonPath("$.message").value("로그인이 완료되었습니다."));

        }

        @Test
        @DisplayName("존재 하지 않는 사용자는 로그인 할 수 없다.")
        void _willFail() throws Exception {
            SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                    .email("")
                    .password("testTEST12!@")
                    .build();
            mockMvc.perform(
                            post("/api/members/signin")
                                    .with(user(makeUserInfo()))
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(signInRequestDTO))
                    )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.message").value("이메일을 입력하세요."));
        }

        @Test
        @DisplayName(" 이메일 형식에 맞지 않아 할 수 없다. ")
        void _willFail_noMember () throws Exception {
            SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                    .email("test")
                    .password("testTEST12!@")
                    .build();
            mockMvc.perform(
                            post("/api/members/signin")
                                    .with(user(makeUserInfo()))
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(signInRequestDTO))
                    )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.message").value("이메일 형식에 맞지 않습니다."));
        }
    }
}