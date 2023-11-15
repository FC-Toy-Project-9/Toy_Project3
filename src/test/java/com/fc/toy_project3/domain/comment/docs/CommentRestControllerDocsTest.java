package com.fc.toy_project3.domain.comment.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.toy_project3.docs.RestDocsSupport;
import com.fc.toy_project3.domain.comment.controller.CommentRestController;
import com.fc.toy_project3.domain.comment.dto.request.CommentCreateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.service.CommentService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;

public class CommentRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    CommentService commentService;

    @Override
    public Object initController() {
        return new CommentRestController(commentService);
    }

    private final ConstraintDescriptions createCommentConstraints = new ConstraintDescriptions(
        CommentCreateRequestDTO.class);

    private final ConstraintDescriptions updateCommentConstraints = new ConstraintDescriptions(
        CommentUpdateRequestDTO.class);


    @Test
    @DisplayName("postComment()는 여행 댓글 정보를 저장할 수 있다.")
    void postComment() throws Exception {
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

        // when, then
        mockMvc.perform(
            post("/api/comments").with(user(customUserDetails)).with(csrf())
                .content(objectMapper.writeValueAsString(commentCreateRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andDo(
            restDoc.document(requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자")
                        .attributes(key("constraints")
                            .value(createCommentConstraints.descriptionsForProperty("tripId"))),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("댓글")
                        .attributes(key("constraints")
                            .value(createCommentConstraints.descriptionsForProperty("content")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                        .description("댓글 생성일"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.NULL)
                        .description("댓글 수정일"))));

        verify(commentService, times(1)).postComment((any(Long.TYPE)),
            any(CommentCreateRequestDTO.class));
    }

    @Test
    @DisplayName("patchComment()는 여행 댓글 정보를 수정할 수 있다.")
    void patchComment() throws Exception {
        // given
        CommentUpdateRequestDTO commentUpdateRequestDTO = CommentUpdateRequestDTO.builder()
            .content("여행 잘 다녀와.").build();
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder().tripId(1L)
            .memberId(1L)
            .nickname("닉네임1")
            .content("여행 잘 다녀와.")
            .createdAt(DateTypeFormatterUtil.localDateTimeToString(LocalDateTime.now()))
            .updatedAt(DateTypeFormatterUtil.localDateTimeToString(LocalDateTime.now())).build();
        given(
            commentService.patchComment(any(Long.TYPE), any(Long.TYPE),
                any(CommentUpdateRequestDTO.class))).willReturn(
            commentResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(
            patch("/api/comments/{commentId}", 1L).with(user(customUserDetails)).with(csrf())
                .content(
                    objectMapper.writeValueAsString(commentUpdateRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
            restDoc.document(pathParameters(parameterWithName("commentId").description("댓글 식별자")),
                requestFields(
                    fieldWithPath("content").description("댓글").attributes(key("constraints")
                        .value(updateCommentConstraints.descriptionsForProperty("content")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                        .description("댓글 생성일"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING)
                        .description("댓글 수정일"))));

        verify(commentService, times(1)).patchComment(any(Long.TYPE), any(Long.TYPE),
            any(CommentUpdateRequestDTO.class));
    }

    @Test
    @DisplayName("softDeleteComment()는 여행 댓글 정보를 삭제할 수 있다.")
    void softDeleteComment() throws Exception {
        // given
        CommentDeleteResponseDTO commentDeleteResponseDTO = CommentDeleteResponseDTO.builder()
            .commentId(1L).build();
        given(commentService.softDeleteComment(any(Long.TYPE), any(Long.TYPE))).willReturn(
            commentDeleteResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(
                delete("/api/comments/{commentId}", 1L).with(user(customUserDetails)).with(csrf()))
            .andExpect(status().isOk()).andDo(
                restDoc.document(pathParameters(parameterWithName("commentId").description("댓글 식별자")),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.commentId").type(JsonFieldType.NUMBER)
                            .description("댓글 식별자"))));
    }
}
