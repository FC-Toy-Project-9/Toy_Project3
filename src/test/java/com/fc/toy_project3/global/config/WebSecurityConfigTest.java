package com.fc.toy_project3.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
        memberRepository.save(new Member((long) 1L, "ypd06021@naver.com", "asdfQWER1!", "fastcam", "패캠"));
    }

    @Test
    @WithMockUser(username = "ypd06021@naver.com", password = "{bcrypt}$2a$10$AXubVWjicX7CXOu94iA2j.yHFq1QmBb0vvIKszO/Hik7P38hEp8KK")
    void testAuthentication() throws Exception {

        SignUpResponseDTO signUpResponseDTO = SignUpResponseDTO.builder()
                .memberId(1L)
                .email("ypd06021@naver.com")
                .nickname("fastcam")
                .name("패캠")
                .build();

        when(memberService.userInfo()).thenReturn(signUpResponseDTO);

        mockMvc.perform(
                        post("/api/members/test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signUpResponseDTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.email").value("ypd06021@naver.com"))
                .andExpect(jsonPath("$.data.name").value("패캠"))
                .andExpect(jsonPath("$.data.nickname").value("fastcam"))
                .andExpect(jsonPath("$.message").value("성공"));
    }

    @Test
    void testWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/members/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}