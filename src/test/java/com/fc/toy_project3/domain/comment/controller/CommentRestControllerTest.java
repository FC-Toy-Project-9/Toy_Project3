package com.fc.toy_project3.domain.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.comment.dto.request.CommentCreateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.service.CommentService;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
            Long mebmerId = 1L;
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

            // when
            mockMvc.perform(
                    post("/api/comments").content(
                            new ObjectMapper().writeValueAsString(commentCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.content").exists()).andDo(print());
            // then
            verify(commentService, times(1)).postComment((any(Long.TYPE)),
                any(CommentCreateRequestDTO.class));

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
                .content("여행 잘 다녀와.").build();
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

            // when
            mockMvc.perform(patch("/api/comments/{commentId}", 1L).content(
                        new ObjectMapper().writeValueAsString(commentUpdateRequestDTO))
                    .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.content").exists()).andDo(print());

            // then
            verify(commentService, times(1)).patchComment((any(Long.TYPE)), (any(Long.TYPE)),
                any(CommentUpdateRequestDTO.class));
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

            // when
            mockMvc.perform(patch("/api/comments/{commentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.commentId").exists()).andDo(print());

            // then
            verify(commentService, times(1)).softDeleteComment((any(Long.TYPE)), (any(Long.TYPE)));
        }
    }
}
