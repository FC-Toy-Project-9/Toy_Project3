package com.fc.toy_project3.domain.member.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.docs.RestDocsSupport;
import com.fc.toy_project3.domain.member.controller.MemberRestController;
import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private MemberService memberService;
    @Override
    public Object initController() {
        return new MemberRestController(memberService);
    }

    private final ConstraintDescriptions signupDescriptions = new ConstraintDescriptions(SignUpResponseDTO.class);
    private final ConstraintDescriptions signinDescriptions = new ConstraintDescriptions(JwtResponseDTO.class);
    @Test
    @DisplayName("회원가입을 할 수 있다.")
    void signup_willSuccess() throws Exception {
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

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/signup")
                .with(user(makeUserInfo()))
                .with(csrf())
                .content(new ObjectMapper().writeValueAsString(signUpRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andDo(
                restDoc.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                        ),
                        responseFields(responseCommon()).and(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("memberId"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름")
                        )));
    }
    @Test
    @DisplayName("로그인 할 수 있다.")
    void signin_willSuccess() throws Exception {
        SignInRequestDTO signInRequestDTO = SignInRequestDTO.builder()
                .email("test@mail.com")
                .password("testTEST12!@")
                .build();
        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .jwtToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5cGQwNjAyMUBuYXZlci5jb20iLCJpYXQiOjE2OTk5NDg0NTMsImV4cCI6MTY5OTk1MDI1M30.RZ4c-kUSeeuaxOSH9mbCn7DG-jOCq5Mw052Wvn8zlxuslNjEGaaVXVT9r4H66GELMm__S1lQsX3UpoujhIbV5w")
                .build();
        given(memberService.signIn(signInRequestDTO)).willReturn(jwtResponseDTO);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/signin")
                .with(user(makeUserInfo()))
                .with(csrf())
                .content(new ObjectMapper().writeValueAsString(signInRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
                restDoc.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(responseCommon()).and(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional(),
                                fieldWithPath("data.jwtToken").type(JsonFieldType.STRING).description("jwt 토큰").optional())));
    }

    private CustomUserDetails makeUserInfo() {
        return new CustomUserDetails(1L, "test", "test@mail.com", "test");
    }

}
