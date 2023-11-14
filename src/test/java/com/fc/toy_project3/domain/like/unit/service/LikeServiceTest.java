package com.fc.toy_project3.domain.like.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.exception.LikeNotFoundException;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.service.TripService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TripService tripService;

    @Nested
    @DisplayName("createLike()은")
    class Context_createLike {

        @Test
        @DisplayName("좋아요 정보를 저장할 수 있다.")
        void _willSuccess() {
            //given
            LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder().tripId(1L).build();
            Long memberId = 1L;

            Member member = Member.builder().id(1L).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(1L).trip(trip).member(member).build();

            given(likeRepository.save(any(Like.class))).willReturn(like);
            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.ofNullable(member));
            given(tripService.getTrip(any(Long.TYPE))).willReturn(trip);

            // when
            LikeResponseDTO result = likeService.createLike(memberId, likeRequestDTO);

            // then
            assertThat(result).extracting("tripId", "memberId").containsExactly(1L, 1L);
            verify(likeRepository, times(1)).save(any(Like.class));
        }
    }

    @Nested
    @DisplayName("getLikeByMemberIdAndTripId()는 ")
    class Context_getLikeByMemberIdAndTripId {

        @Test
        @DisplayName("특정 회원 id와 여행 id를 가진 좋아요 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            Long memberId = 1L; Long tripId = 1L;
            Member member = Member.builder().id(1L).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(1L).trip(trip).member(member).build();
            given(likeRepository.findByMemberIdAndTripId(any(Long.TYPE),any(Long.TYPE))).willReturn(like);

            // when
            LikeResponseDTO result = likeService.getLikeByMemberIdAndTripId(memberId, tripId);

            // then
            assertThat(result).extracting("tripId", "memberId").containsExactly(1L, 1L);
            verify(likeRepository, times(1)).findByMemberIdAndTripId(any(Long.TYPE), any(Long.TYPE));
        }

        @Test
        @DisplayName("특정 회원 id와 여행 id를 가진 좋아요 정보를 찾을 수 없으면 조회할 수 없다.")
        void likeNotFound_willFail() {
            // given
            Like like = null;
            given(likeRepository.findByMemberIdAndTripId(any(Long.TYPE), any(Long.TYPE))).willReturn(like);

            // when, then
            Throwable exception = assertThrows(LikeNotFoundException.class, () -> {
                likeService.getLikeByMemberIdAndTripId(1L, 1L);
            });
            assertEquals("좋아요 정보를 찾을 수 없습니다.", exception.getMessage());
            verify(likeRepository, times(1)).findByMemberIdAndTripId(any(Long.TYPE), any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("deleteLikeById()는 ")
    class Context_deleteTripById {

        @Test
        @DisplayName("특정 id를 가진 좋아요 정보를 삭제할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder().id(1L).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(1L).trip(trip).member(member).build();
            given(likeRepository.findById(any(Long.TYPE))).willReturn(Optional.of(like));

            // when
            likeService.deleteLikeById(1L, 1L);

            // then
            verify(likeRepository, times(1)).findById(any(Long.TYPE));
            verify(likeRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("특정 id를 가진 좋아요 정보를 찾을 수 없으면 삭제할 수 없다.")
        void tripNotFound_willFail() {
            // given
            Optional<Like> like = Optional.empty();
            given(likeRepository.findById(any(Long.TYPE))).willReturn(like);

            // when, then
            Throwable exception = assertThrows(LikeNotFoundException.class, () -> {
                likeService.deleteLikeById(1L, 1L);
            });
            assertEquals("좋아요 정보를 찾을 수 없습니다.", exception.getMessage());
            verify(likeRepository, times(1)).findById(any(Long.TYPE));
            verify(likeRepository, never()).deleteById(any(Long.TYPE));
        }
    }
}