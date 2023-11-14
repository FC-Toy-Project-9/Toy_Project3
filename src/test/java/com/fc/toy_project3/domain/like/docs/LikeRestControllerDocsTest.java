package com.fc.toy_project3.domain.like.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.docs.RestDocsSupport;
import com.fc.toy_project3.domain.like.controller.LikeRestController;
import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

public class LikeRestControllerDocsTest extends RestDocsSupport {
    @MockBean
    private LikeService likeService;

    @Override
    public Object initController() {
        return new LikeRestController(likeService);
    }

    private final ConstraintDescriptions descriptions = new ConstraintDescriptions(LikeRequestDTO.class);

    @Test
    @DisplayName("createLike()은 좋아요 정보를 저장할 수 있다.")
    void createLike() throws Exception {
        // given
        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder().tripId(1L).build();
        Long memberId = 1L;
        LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(1L).memberId(memberId).tripId(likeRequestDTO.getTripId()).build();

        given(likeService.createLike(memberId, likeRequestDTO)).willReturn(likeResponseDTO);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/likes")
            .content(new ObjectMapper().writeValueAsString(likeRequestDTO))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andDo(
            restDoc.document(requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자")
                        .attributes(key("constraints").value(descriptions.descriptionsForProperty("tripId")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.likeId").type(JsonFieldType.NUMBER).description("좋아요 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"))));
        verify(likeService, times(1)).createLike(any(Long.TYPE), any(LikeRequestDTO.class));
    }

    @Test
    @DisplayName("getLikeByMemberIdAndTripId()는 특정 회원 id와 특정 여행 id를 가진 좋아요 정보를 조회할 수 있다.")
    void getLikeByMemberIdAndTripId() throws Exception {
        // given
        Long memberId = 1L, tripId = 1L;
        LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(1L).memberId(memberId).tripId(tripId).build();

        given(likeService.getLikeByMemberIdAndTripId(memberId, tripId)).willReturn(likeResponseDTO);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/likes/{tripId}", 1L))
            .andExpect(status().isOk()).andDo(
                restDoc.document(pathParameters(parameterWithName("tripId").description("여행 식별자")),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.likeId").type(JsonFieldType.NUMBER).description("좋아요 식별자"),
                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                        fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"))));
        verify(likeService, times(1)).getLikeByMemberIdAndTripId(any(Long.TYPE), any(Long.TYPE));
    }

    @Test
    @DisplayName("deleteLikeById()은 특정 id를 가진 좋아요 정보를 삭제할 수 있다.")
    void deleteLikeById() throws Exception {
        //given
        Long likeId = 1L; Long memberId = 1L;
        LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(1L).tripId(1L).build();

        given(likeService.deleteLikeById(memberId, likeId)).willReturn(likeResponseDTO);

        //when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/likes/{likeId}", 1L))
            .andExpect(status().isOk()).andDo(
                restDoc.document(pathParameters(parameterWithName("likeId").description("좋아요 식별자")),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.likeId").type(JsonFieldType.NUMBER).description("좋아요 식별자"),
                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                        fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"))));
        verify(likeService, times(1)).deleteLikeById(memberId, likeId);
    }
}

