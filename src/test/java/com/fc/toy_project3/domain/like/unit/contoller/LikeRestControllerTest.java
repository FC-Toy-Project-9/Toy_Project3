package com.fc.toy_project3.domain.like.unit.contoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.like.controller.LikeRestController;
import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LikeRestController.class)
public class LikeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Nested
    @DisplayName("createLike()는")
    class Context_createLike {

        @Test
        @DisplayName("좋아요 정보를 등록할 수 있다.")
        @WithMockUser
        void _willSuccess() throws Exception {
            //given
            Long memberId = 1L;
            Long tripId = 1L;
            Long likeId = 1L;
            LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder().tripId(tripId).build();
            LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(memberId).tripId(tripId).build();

            given(likeService.createLike(memberId, likeRequestDTO)).willReturn(likeResponseDTO);

            CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com","test");

            //when, then
            mockMvc.perform(post("/api/likes").with(user(customUserDetails)).with(csrf())
                .content(new ObjectMapper().writeValueAsString(likeRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").exists()).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.likeId").exists())
                .andExpect(jsonPath("$.data.memberId").exists())
                .andExpect(jsonPath("$.data.tripId").exists())
                .andDo(print());
            verify(likeService, times(1)).createLike(any(Long.TYPE), any(LikeRequestDTO.class));
        }

        @Nested
        @DisplayName("tripId가 ")
        class Element_tripId {

            @Test
            @DisplayName("null일 경우 좋아요 정보를 저장할 수 없다.")
            void null_willFail() throws Exception {
                // given
                LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder().tripId(null).build();

                CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com","test");

                // when, then
                mockMvc.perform(post("/api/trip").with(user(customUserDetails)).with(csrf())
                        .content(new ObjectMapper().writeValueAsString(likeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                    .andDo(print());
                verify(likeService, never()).createLike(any(Long.TYPE), any(LikeRequestDTO.class));
            }
        }
    }

    @Nested
    @DisplayName("getLikeByMemberIdAndTripId()는")
    class Context_getLikeByMemberIdAndTripId {

        @Test
        @DisplayName("특정 회원 id와 여행 id를 가진 좋아요 정보를 조회할 수 있다.")
        void _willSuccess() throws Exception {
            //given
            Long memberId = 1L;
            Long tripId = 1L;
            Long likeId = 1L;
            LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(memberId).tripId(tripId).build();

            given(likeService.getLikeByMemberIdAndTripId(memberId, tripId)).willReturn(likeResponseDTO);

            CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com","test");

            //when, then
            mockMvc.perform(get("/api/likes/{tripId}", 1L).with(user(customUserDetails)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists()).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.likeId").exists())
                .andExpect(jsonPath("$.data.memberId").exists())
                .andExpect(jsonPath("$.data.tripId").exists())
                .andDo(print());
            verify(likeService, times(1)).getLikeByMemberIdAndTripId(any(Long.TYPE), any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("deleteLikeById()는")
    class Context_deleteLikeById {

        @Test
        @DisplayName("특정 id를 가진 좋아요 정보를 삭제할 수 있다.")
        void _willSuccess() throws Exception {
            //given
            Long likeId = 1L;
            Long memberId = 1L;
            Long tripId = 1L;
            LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(memberId).tripId(tripId).build();

            given(likeService.deleteLikeById(memberId, tripId)).willReturn(likeResponseDTO);

            CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com","test");

            //when, then
            mockMvc.perform(delete("/api/likes/{likeId}", 1L).with(user(customUserDetails)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists()).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.likeId").exists())
                .andExpect(jsonPath("$.data.memberId").exists())
                .andExpect(jsonPath("$.data.tripId").exists())
                .andDo(print());
            verify(likeService, times(1)).deleteLikeById(any(Long.TYPE), any(Long.TYPE));
        }
    }
}
