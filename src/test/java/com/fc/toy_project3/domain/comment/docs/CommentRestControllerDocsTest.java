package com.fc.toy_project3.domain.comment.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.docs.RestDocsSupport;
import com.fc.toy_project3.domain.comment.controller.CommentRestController;
import com.fc.toy_project3.domain.comment.dto.request.CommentRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

public class CommentRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    CommentService commentService;

    @Override
    public Object initController() {
        return new CommentRestController(commentService);
    }

    @Test
    @DisplayName("postComment()는 여행 댓글 정보를 저장할 수 있다.")
    void postComment() throws Exception {
        //given
        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder().tripId(1L).memberId(1L)
            .content("여행 계획 정말 멋있다.").build();
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L).memberId(1L)
            .content("여행 계획 정말 멋있다.").build();
        given(commentService.postComment(any(CommentRequestDTO.class))).willReturn(
            commentResponseDTO);

        //when, then
        mockMvc.perform(post("/api/comments")
            .content(new ObjectMapper().writeValueAsString(commentRequestDTO))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andDo(
            restDoc.document(requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("댓글")),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글"))));

        verify(commentService, times(1)).postComment((any(CommentRequestDTO.class)));
    }

    @Test
    @DisplayName("patchComment()는 여행 댓글 정보를 수정할 수 있다.")
    void patchComment() throws Exception {
        //given
        CommentUpdateRequestDTO commentUpdateRequestDTO = CommentUpdateRequestDTO.builder()
            .content("여행 잘 다녀와.").build();
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L).memberId(1L)
            .content("여행 잘 다녀와.").build();
        given(
            commentService.patchComment(any(Long.TYPE),
                any(CommentUpdateRequestDTO.class))).willReturn(
            commentResponseDTO);

        //when, then
        mockMvc.perform(patch("/api/comments/{commentId}", 1L)
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
            restDoc.document(requestFields(
                    fieldWithPath("commentId").description("댓글 식별자"),
                    fieldWithPath("content").description("댓글")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글"))));

        verify(commentService, times(1)).patchComment((any(Long.TYPE)),
            any(CommentUpdateRequestDTO.class));


    }

    @Test
    @DisplayName("softDeleteComment()는 여행 댓글 정보를 삭제할 수 있다.")
    void softDeleteComment() throws Exception {
        //given
        doNothing().when(commentService).softDeleteComment(any(Long.TYPE));

        //when, then
        mockMvc.perform(delete("api/comments/{commentId}",1L)).andExpect(status().isOk()).andDo(
            restDoc.document(pathParameters(parameterWithName("commentId").description("댓글 식별자")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.replyId").type(JsonFieldType.NUMBER).description("댓글 식별자"))));

    }
}
