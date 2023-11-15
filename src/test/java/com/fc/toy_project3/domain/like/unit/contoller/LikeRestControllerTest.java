package com.fc.toy_project3.domain.like.unit.contoller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.fc.toy_project3.domain.like.controller.LikeRestController;
import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.global.common.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LikeRestControllerTest {

    @InjectMocks
    private LikeRestController likeRestController;

    @Mock
    private LikeService likeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createLike()는")
    class Context_createLike {

        @Test
        @DisplayName("좋아요 정보를 등록할 수 있다.")
        void _willSuccess() {
            //given
            Long memberId = 1L;
            Long tripId = 1L;
            Long likeId = 1L;
            LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder().tripId(tripId).build();
            LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(memberId).tripId(tripId).build();

            given(likeService.createLike(memberId, likeRequestDTO)).willReturn(likeResponseDTO);

            //when
            ResponseEntity<ResponseDTO<LikeResponseDTO>> responseEntity = likeRestController.createLike(likeRequestDTO);

            //then
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody().getData());
            assertEquals(memberId, responseEntity.getBody().getData().getMemberId());
            assertEquals(likeRequestDTO.getTripId(), responseEntity.getBody().getData().getTripId());
            verify(likeService, times(1)).createLike(memberId, likeRequestDTO);
        }
    }

    @Nested
    @DisplayName("getLikeByMemberIdAndTripId()는")
    class Context_getLikeByMemberIdAndTripId {

        @Test
        @DisplayName("특정 회원 id와 여행 id를 가진 좋아요 정보를 조회할 수 있다.")
        void _willSuccess() {
            //given
            Long memberId = 1L;
            Long tripId = 1L;
            Long likeId = 1L;
            LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(memberId).tripId(tripId).build();

            given(likeService.getLikeByMemberIdAndTripId(memberId, tripId)).willReturn(likeResponseDTO);

            //when
            ResponseEntity<ResponseDTO<LikeResponseDTO>> responseEntity = likeRestController.getLikeByMemberIdAndTripId(tripId);

            //then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody().getData());
            assertEquals(memberId, responseEntity.getBody().getData().getMemberId());
            assertEquals(tripId, responseEntity.getBody().getData().getTripId());
            verify(likeService, times(1)).getLikeByMemberIdAndTripId(any(Long.TYPE), any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("deleteLikeById()는")
    class Context_deleteLikeById {

        @Test
        @DisplayName("특정 id를 가진 좋아요 정보를 삭제할 수 있다.")
        void _willSuccess() {
            //given
            Long likeId = 1L;
            Long memberId = 1L;
            Long tripId = 1L;
            LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder().likeId(likeId).memberId(memberId).tripId(tripId).build();

            given(likeService.deleteLikeById(memberId, tripId)).willReturn(likeResponseDTO);

            //when
            ResponseEntity<ResponseDTO<LikeResponseDTO>> responseEntity = likeRestController.deleteLikeById(likeId);

            //then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody().getData());
            assertEquals(likeId, responseEntity.getBody().getData().getLikeId());
            assertEquals(memberId, responseEntity.getBody().getData().getMemberId());
            verify(likeService, times(1)).deleteLikeById(any(Long.TYPE), any(Long.TYPE));
        }
    }
}
