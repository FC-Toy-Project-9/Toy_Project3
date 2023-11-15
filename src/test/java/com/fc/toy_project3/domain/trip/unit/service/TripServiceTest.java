package com.fc.toy_project3.domain.trip.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.TripPageRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.exception.InvalidTripDateRangeException;
import com.fc.toy_project3.domain.trip.exception.TripNotFoundException;
import com.fc.toy_project3.domain.trip.exception.WrongTripStartDateException;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import com.fc.toy_project3.domain.trip.service.TripService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Trip Service Test
 */
@Transactional
@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @InjectMocks
    private TripService tripService;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private LikeRepository likeRepository;

    @Nested
    @DisplayName("postTrip()은")
    class Context_postTrip {

        @Test
        @DisplayName("여행 정보를 저장할 수 있다.")
        void _willSuccess() {
            //given
            PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                .tripName("제주도 여행")
                .startDate("2023-10-25")
                .endDate("2023-10-26")
                .isDomestic(true)
                .build();
            Member member = Member.builder().id(1L).nickname("닉네임").build();
            Trip trip = Trip.builder()
                .id(1L)
                .member(member)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(member);
            given(tripRepository.save(any(Trip.class))).willReturn(trip);

            // when
            TripResponseDTO result = tripService.postTrip(postTripRequestDTO, 1L);

            // then
            assertThat(result).extracting("tripId", "memberId", "nickname", "tripName", "startDate",
                    "endDate", "isDomestic")
                .containsExactly(1L, 1L, "닉네임", "제주도 여행", "2023-10-25", "2023-10-26", true);
            verify(tripRepository, times(1)).save(any(Trip.class));
        }

        @Test
        @DisplayName("여행 종료일이 시작일보다 빠르면 여행 정보를 저장할 수 없다.")
        void InvalidTripDateRange_willFail() {
            // given
            PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                .tripName("제주도 여행")
                .startDate("2023-10-26")
                .endDate("2023-10-25")
                .isDomestic(true)
                .build();

            // when, then
            Throwable exception = assertThrows(InvalidTripDateRangeException.class, () -> {
                tripService.postTrip(postTripRequestDTO, 1L);
            });
            assertEquals("시작일이 종료일보다 빨라야 합니다.", exception.getMessage());
            verify(tripRepository, never()).save(any(Trip.class));
        }
    }

    @Nested
    @DisplayName("getTrips()은 ")
    class Context_getTrips {

        @Test
        @DisplayName("여행 정보 목록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            GetTripsRequestDTO getTripsRequestDTO = GetTripsRequestDTO.builder()
                .tripName("제주도")
                .build();
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("createdAt")
                .sort("ASC")
                .build().of();
            Member member1 = Member.builder().id(1L).nickname("닉네임1").build();
            Member member2 = Member.builder().id(2L).nickname("닉네임2").build();
            Member member3 = Member.builder().id(3L).nickname("닉네임3").build();
            Trip trip1 = Trip.builder()
                .id(1L)
                .member(member1)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 23))
                .endDate(LocalDate.of(2023, 10, 27))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Trip trip2 = Trip.builder()
                .id(2L)
                .member(member2)
                .name("속초 겨울바다 여행")
                .startDate(LocalDate.of(2023, 11, 27))
                .endDate(LocalDate.of(2023, 11, 29))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Trip trip3 = Trip.builder()
                .id(3L)
                .member(member3)
                .name("크리스마스 미국 여행")
                .startDate(LocalDate.of(2023, 12, 24))
                .endDate(LocalDate.of(2023, 12, 26))
                .isDomestic(false)
                .itineraries(new ArrayList<>())
                .build();
            given(tripRepository.findAllBySearchCondition(any(GetTripsRequestDTO.class), any(
                Pageable.class))).willReturn(new PageImpl<>(List.of(trip1, trip2, trip3)));

            // when
            GetTripsResponseDTO result = tripService.getTrips(getTripsRequestDTO, pageable);

            // then
            assertThat(result.getTrips()).isNotEmpty();
            verify(tripRepository, times(1)).findAllBySearchCondition(any(GetTripsRequestDTO.class),
                any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("getLikedTrips()은 ")
    class Context_getLikedTrips {

        @Test
        @DisplayName("회원이 좋아요 한 여행 정보 목록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            long memberId = 1L;
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("createdAt")
                .sort("ASC")
                .build().of();
            Member member1 = Member.builder().id(1L).nickname("닉네임1").build();
            Member member2 = Member.builder().id(2L).nickname("닉네임2").build();
            Member member3 = Member.builder().id(3L).nickname("닉네임3").build();
            Trip trip1 = Trip.builder()
                .id(1L)
                .member(member1)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 23))
                .endDate(LocalDate.of(2023, 10, 27))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Trip trip2 = Trip.builder()
                .id(2L)
                .member(member2)
                .name("속초 겨울바다 여행")
                .startDate(LocalDate.of(2023, 11, 27))
                .endDate(LocalDate.of(2023, 11, 29))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Trip trip3 = Trip.builder()
                .id(3L)
                .member(member3)
                .name("크리스마스 미국 여행")
                .startDate(LocalDate.of(2023, 12, 24))
                .endDate(LocalDate.of(2023, 12, 26))
                .isDomestic(false)
                .itineraries(new ArrayList<>())
                .build();
            Like like1 = Like.builder()
                .id(1L)
                .trip(trip1)
                .member(member1)
                .build();
            Like like2 = Like.builder()
                .id(2L)
                .trip(trip2)
                .member(member1)
                .build();
            Like like3 = Like.builder()
                .id(3L)
                .trip(trip3)
                .member(member1)
                .build();
            given(likeRepository.findAllByMemberIdAndPageable(any(Long.class), any(
                Pageable.class))).willReturn(new PageImpl<>(List.of(like1, like2, like3)));

            // when
            GetTripsResponseDTO result = tripService.getLikedTrips(memberId, pageable);

            // then
            assertThat(result.getTrips()).isNotEmpty();
            verify(likeRepository, times(1)).findAllByMemberIdAndPageable(any(Long.TYPE),
                any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("getTripById()는 ")
    class Context_getTripById {

        @Test
        @DisplayName("여행 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder().id(1L).nickname("닉네임").build();
            Optional<Trip> trip = Optional.of(Trip.builder()
                .id(1L)
                .member(member)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 23))
                .endDate(LocalDate.of(2023, 10, 27))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build());
            given(tripRepository.findById(any(Long.TYPE))).willReturn(trip);

            // when
            GetTripResponseDTO result = tripService.getTripById(1L);

            // then
            assertThat(result).extracting("tripId", "memberId", "nickname", "tripName", "startDate",
                    "endDate", "isDomestic")
                .containsExactly(1L, 1L, "닉네임", "제주도 여행", "2023-10-23", "2023-10-27", true);
            verify(tripRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("여행 정보를 찾을 수 없으면 조회할 수 없다.")
        void tripNotFound_willFail() {
            // given
            Optional<Trip> trip = Optional.empty();
            given(tripRepository.findById(any(Long.TYPE))).willReturn(trip);

            // when, then
            Throwable exception = assertThrows(TripNotFoundException.class, () -> {
                tripService.getTripById(1L);
            });
            assertEquals("여행 기록을 찾을 수 없습니다.", exception.getMessage());
            verify(tripRepository, times(1)).findById(any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("updateTrip()은 ")
    class Context_updateTrip {

        @Test
        @DisplayName("여행 정보를 수정할 수 있다.")
        void _willSuccess() {
            // given
            UpdateTripRequestDTO updateTripRequestDTO = UpdateTripRequestDTO.builder().tripId(1L)
                .tripName("울릉도 여행").startDate("2023-10-25").endDate("2023-10-26").isDomestic(true)
                .build();
            Member member = Member.builder().id(1L).nickname("닉네임").build();
            Optional<Trip> trip = Optional.of(
                Trip.builder().id(1L).member(member).name("제주도 여행")
                    .startDate(LocalDate.of(2023, 10, 23))
                    .endDate(LocalDate.of(2023, 10, 27)).isDomestic(true)
                    .itineraries(new ArrayList<>()).build());
            given(tripRepository.findById(any(Long.TYPE))).willReturn(trip);

            // when
            TripResponseDTO result = tripService.updateTrip(updateTripRequestDTO, member.getId());

            // then
            assertThat(result).extracting("tripId", "memberId", "nickname", "tripName", "startDate",
                    "endDate",
                    "isDomestic")
                .containsExactly(1L, 1L, "닉네임", "울릉도 여행", "2023-10-25", "2023-10-26", true);
            verify(tripRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("여행 시작일이 알맞지 않으면 수정할 수 없다.")
        void WrongStartDate_willFail() {
            // given
            UpdateTripRequestDTO updateTripRequestDTO = UpdateTripRequestDTO.builder().tripId(1L)
                .tripName("울릉도 여행").startDate("2023-10-25").endDate("2023-10-24").isDomestic(true)
                .build();
            Member member = Member.builder().id(1L).nickname("닉네임").build();
            Optional<Trip> trip = Optional.of(
                Trip.builder().id(1L).member(member).name("제주도 여행")
                    .startDate(LocalDate.of(2023, 10, 23))
                    .endDate(LocalDate.of(2023, 10, 27)).isDomestic(true)
                    .itineraries(new ArrayList<>()).build());
            given(tripRepository.findById(any(Long.TYPE))).willReturn(trip);

            // when, then
            Throwable exception = assertThrows(WrongTripStartDateException.class, () -> {
                tripService.updateTrip(updateTripRequestDTO, member.getId());
            });
            assertEquals("여행 시작일을 다시 확인해주세요.", exception.getMessage());
            verify(tripRepository, times(1)).findById(any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("deleteTripById()는 ")
    class Context_deleteTripById {

        @Test
        @DisplayName("특정 id를 가진 여행 정보를 삭제할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder().id(1L).nickname("닉네임").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            given(tripRepository.findById(any(Long.TYPE))).willReturn(Optional.of(trip));

            // when
            TripResponseDTO tripResponseDTO = tripService.deleteTripById(trip.getId(), member.getId());

            // then
            assertThat(tripResponseDTO.getTripId()).isEqualTo(1);
            verify(tripRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("특정 id를 가진 여행 정보를 찾을 수 없으면 삭제할 수 없다.")
        void tripNotFound_willFail() {
            // given
            Optional<Trip> trip = Optional.empty();
            given(tripRepository.findById(any(Long.TYPE))).willReturn(trip);

            // when, then
            Throwable exception = assertThrows(TripNotFoundException.class, () -> {
                tripService.getTripById(1L);
            });
            assertEquals("여행 기록을 찾을 수 없습니다.", exception.getMessage());
            verify(tripRepository, times(1)).findById(any(Long.TYPE));
            verify(tripRepository, never()).delete(any(Trip.class));
        }
    }
}
