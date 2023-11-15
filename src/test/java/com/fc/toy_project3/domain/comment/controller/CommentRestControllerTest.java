package com.fc.toy_project3.domain.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.comment.dto.request.CommentCreateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.service.CommentService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentRestController.class)
public class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Nested
    @DisplayName("postComment()는")
    class Context_postComment {

        @Test
        @DisplayName("여행 댓글을 저장할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            CommentCreateRequestDTO commentCreateRequestDTO = CommentCreateRequestDTO.builder()
                .tripId(1L)
                .content("여행 계획 정말 멋있다.").build();
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L)
                .memberId(1L)
                .nickname("닉네임1")
                .content("여행 계획 정말 멋있다.")
                .createdAt(DateTypeFormatterUtil.localDateTimeToString(LocalDateTime.now()))
                .updatedAt(null).build();
            given(commentService.postComment(any(Long.TYPE),
                any(CommentCreateRequestDTO.class))).willReturn(
                commentResponseDTO);
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
                "test");

            // when
            mockMvc.perform(
                    post("/api/comments").with(user(customUserDetails)).with(csrf()).content(
                            new ObjectMapper().writeValueAsString(commentCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.content").exists()).andDo(print());

            // then
            verify(commentService, times(1)).postComment((any(Long.TYPE)),
                any(CommentCreateRequestDTO.class));
        }

        @Nested
        @DisplayName("tripId가 ")
        class Element_tripId {

            @Test
            @DisplayName("null일 경우 댓글을 저장할 수 없다.")
            void null_willFail() throws Exception {
                // given
                CommentCreateRequestDTO commentCreateRequestDTO = CommentCreateRequestDTO.builder()
                    .tripId(null)
                    .content("여행 계획 정말 멋있다.").build();
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test",
                    "test@mail.com",
                    "test");

                // when
                mockMvc.perform(
                        post("/api/comments").with(user(customUserDetails)).with(csrf()).content(
                                new ObjectMapper().writeValueAsString(commentCreateRequestDTO))
                            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                    .andDo(print());

                // then
                verify(commentService, never()).postComment((any(Long.TYPE)),
                    any(CommentCreateRequestDTO.class));
            }
        }

        @Nested
        @DisplayName("content가 ")
        class Element_content {

            @Test
            @DisplayName("255자 이상이면 댓글을 저장할 수 없다.")
            void oversize_willFail() throws Exception {
                // given
                CommentCreateRequestDTO commentCreateRequestDTO = CommentCreateRequestDTO.builder()
                    .tripId(1L)
                    .content(
                        "프로그래밍에서 코드의 가독성과 효율성은 항상 고려되어야 합니다. 알고리즘의 선택과 데이터 구조의 활용은 성능에 큰 영향을 미칩니다. 디자인 패턴을 적용하여 유지보수성을 높이고, 테스트 주도 개발을 통해 안정성을 확보할 수 있습니다. 동시에 클라우드 기술과 마이크로서비스 아키텍처를 활용하면 확장성과 유연성을 향상시킬 수 있습니다. 현대 소프트웨어 개발에서는 커뮤니케이션과 협업이 핵심이며, 지속적 통합과 배포를 통해 빠른 개발 주기를 유지하는 것이 중요합니다.")
                    .build();
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test",
                    "test@mail.com",
                    "test");

                // when
                mockMvc.perform(
                        post("/api/comments").with(user(customUserDetails)).with(csrf()).content(
                                new ObjectMapper().writeValueAsString(commentCreateRequestDTO))
                            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                    .andDo(print());

                // then
                verify(commentService, never()).postComment((any(Long.TYPE)),
                    any(CommentCreateRequestDTO.class));
            }
        }
    }

    @Nested
    @DisplayName("patchComment()는")
    class Context_patchComment {

        @Test
        @DisplayName("patchComment()는 여행 댓글을 수정할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            CommentUpdateRequestDTO commentUpdateRequestDTO = CommentUpdateRequestDTO.builder()
                .content("여행 계획 정말 멋있다.").build();
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L)
                .memberId(1L)
                .nickname("닉네임1")
                .content("여행 계획 정말 멋있다.")
                .createdAt(DateTypeFormatterUtil.localDateTimeToString(LocalDateTime.now()))
                .updatedAt(DateTypeFormatterUtil.localDateTimeToString(LocalDateTime.now()))
                .build();
            given(
                commentService.patchComment(any(Long.TYPE), any(Long.TYPE),
                    any(CommentUpdateRequestDTO.class))).willReturn(
                commentResponseDTO);
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
                "test");

            // when
            mockMvc.perform(
                    patch("/api/comments/{commentId}", 1L).with(user(customUserDetails)).with(csrf())
                        .content(
                            new ObjectMapper().writeValueAsString(commentUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.content").exists()).andDo(print());

            // then
            verify(commentService, times(1)).patchComment((any(Long.TYPE)), (any(Long.TYPE)),
                any(CommentUpdateRequestDTO.class));
        }

        @Nested
        @DisplayName("content가 ")
        class Element_content {

            @Test
            @DisplayName("255자 이상이면 댓글을 수정할 수 없다.")
            @WithMockUser
            void oversize_willFail() throws Exception {
                // given
                CommentUpdateRequestDTO commentUpdateRequestDTO = CommentUpdateRequestDTO.builder()
                    .content(
                        "프로그래밍에서 코드의 가독성과 효율성은 항상 고려되어야 합니다. 알고리즘의 선택과 데이터 구조의 활용은 성능에 큰 영향을 미칩니다. 디자인 패턴을 적용하여 유지보수성을 높이고, 테스트 주도 개발을 통해 안정성을 확보할 수 있습니다. 동시에 클라우드 기술과 마이크로서비스 아키텍처를 활용하면 확장성과 유연성을 향상시킬 수 있습니다. 현대 소프트웨어 개발에서는 커뮤니케이션과 협업이 핵심이며, 지속적 통합과 배포를 통해 빠른 개발 주기를 유지하는 것이 중요합니다.")
                    .build();
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test",
                    "test@mail.com",
                    "test");

                // when
                mockMvc.perform(
                        patch("/api/comments/{commentId}", 1L).with(user(customUserDetails))
                            .with(csrf()).content(
                                new ObjectMapper().writeValueAsString(commentUpdateRequestDTO))
                            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                    .andDo(print());

                // then
                verify(commentService, never()).patchComment((any(Long.TYPE)), (any(Long.TYPE)),
                    any(CommentUpdateRequestDTO.class));
            }
        }
    }

    @Nested
    @DisplayName("softDeleteComment()는")
    class Context_softDeleteComment {

        @Test
        @DisplayName("여행 댓글을 수정할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            CommentDeleteResponseDTO commentDeleteResponseDTO = CommentDeleteResponseDTO.builder()
                .commentId(1L).build();
            given(commentService.softDeleteComment(any(Long.TYPE), any(Long.TYPE))).willReturn(
                commentDeleteResponseDTO);
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
                "test");

            // when
            mockMvc.perform(
                    delete("/api/comments/{commentId}", 1L).with(user(customUserDetails)).with(
                        csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.commentId").exists()).andDo(print());

            // then
            verify(commentService, times(1)).softDeleteComment((any(Long.TYPE)), (any(Long.TYPE)));
        }
    }
}
