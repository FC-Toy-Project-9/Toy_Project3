package com.fc.toy_project3.domain.trip.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.domain.itinerary.dto.response.get.GetAccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.get.GetTransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.get.GetVisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.trip.controller.TripRestController;
import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripsResponseDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.service.TripService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Trip REST Controller Test
 */
@WebMvcTest(TripRestController.class)
public class TripRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TripService tripService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("postTrip()은")
    class Context_postTrip {

        @Test
        @DisplayName("여행정보를 저장할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                .tripName("제주도 여행")
                .startDate("2023-10-25")
                .endDate("2023-10-26")
                .isDomestic(true)
                .build();
            TripResponseDTO trip = TripResponseDTO.builder()
                .tripId(1L)
                .memberId(1L)
                .nickname("깜찍이")
                .tripName("제주도 여행")
                .startDate("2023-10-25")
                .endDate("2023-10-26")
                .isDomestic(true)
                .build();
            given(tripService.postTrip(any(PostTripRequestDTO.class), any(long.class))).willReturn(
                trip);

            // when, then
            mockMvc.perform(post("/api/trips")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(postTripRequestDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.tripId").isNumber())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.nickname").isString())
                .andExpect(jsonPath("$.data.tripName").isString())
                .andExpect(jsonPath("$.data.startDate").isString())
                .andExpect(jsonPath("$.data.endDate").isString())
                .andExpect(jsonPath("$.data.isDomestic").isBoolean())
                .andDo(print());
            verify(tripService, times(1)).postTrip(any(PostTripRequestDTO.class), any(Long.TYPE));
        }

        @Nested
        @DisplayName("name이 ")
        class Element_name {

            @Test
            @DisplayName("blank일 경우 여행 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                    .tripName(" ")
                    .startDate("2023-10-25")
                    .endDate("2023-10-26")
                    .isDomestic(true)
                    .build();

                // when, then
                mockMvc.perform(post("/api/trips")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postTripRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
                verify(tripService, never()).postTrip(any(PostTripRequestDTO.class),
                    any(Long.TYPE));
            }
        }

        @Nested
        @DisplayName("startDate가 ")
        class Element_startDate {

            @Test
            @DisplayName("blank일 경우 여행 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                    .tripName("제주도 여행")
                    .startDate(" ")
                    .endDate("2023-10-26")
                    .isDomestic(true)
                    .build();

                // when, then
                mockMvc.perform(post("/api/trips")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postTripRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
                verify(tripService, never()).postTrip(any(PostTripRequestDTO.class),
                    any(Long.TYPE));
            }
        }

        @Nested
        @DisplayName("endDate가 ")
        class Element_endDate {

            @Test
            @DisplayName("blank일 경우 여행 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                    .tripName("제주도 여행")
                    .startDate("2023-10-26")
                    .endDate(" ")
                    .isDomestic(true)
                    .build();

                // when, then
                mockMvc.perform(post("/api/trips")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postTripRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
                verify(tripService, never()).postTrip(any(PostTripRequestDTO.class),
                    any(Long.TYPE));
            }
        }

        @Nested
        @DisplayName("isDomestic이 ")
        class Element_isDomestic {

            @Test
            @DisplayName("null일 경우 여행 정보를 저장할 수 없다.")
            void null_willFail() throws Exception {
                // given
                PostTripRequestDTO postTripRequestDTO = PostTripRequestDTO.builder()
                    .tripName("제주도 여행")
                    .startDate("2023-10-26")
                    .endDate("2023-10-26")
                    .isDomestic(null)
                    .build();

                // when, then
                mockMvc.perform(post("/api/trips")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postTripRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
                verify(tripService, never()).postTrip(any(PostTripRequestDTO.class),
                    any(Long.TYPE));
            }
        }
    }

    @Nested
    @DisplayName("getTrips()은")
    class Context_getTrips {

        @Test
        @DisplayName("여행 정보 목록을 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            List<TripsResponseDTO> trips = new ArrayList<>();
            trips.add(TripsResponseDTO.builder()
                .tripId(1L)
                .memberId(1L)
                .nickname("깜찍이")
                .tripName("제주도 여행")
                .startDate("2023-10-23")
                .endDate("2023-10-27")
                .isDomestic(true)
                .likeCount(7L)
                .createdAt("2023-10-01 10:00")
                .updatedAt(null)
                .build());
            trips.add(TripsResponseDTO.builder()
                .tripId(2L)
                .memberId(2L)
                .nickname("멋쟁이")
                .tripName("속초 겨울바다 여행")
                .startDate("2023-11-27")
                .endDate("2023-11-29")
                .isDomestic(true)
                .likeCount(3L)
                .createdAt("2023-10-01 12:00")
                .updatedAt(null)
                .build());
            trips.add(TripsResponseDTO.builder()
                .tripId(3L)
                .memberId(3L)
                .nickname("꿈틀이")
                .tripName("크리스마스 미국 여행")
                .startDate("2023-12-24")
                .endDate("2023-12-26")
                .isDomestic(false)
                .likeCount(10L)
                .createdAt("2023-10-02 10:00")
                .updatedAt(null)
                .build());
            GetTripsResponseDTO getTripsResponseDTO = GetTripsResponseDTO.builder()
                .totalTrips(1)
                .isLastPage(true)
                .totalTrips(3)
                .trips(trips)
                .build();
            given(tripService.getTrips(any(GetTripsRequestDTO.class),
                any(Pageable.class))).willReturn(getTripsResponseDTO);

            // when, then
            mockMvc.perform(get("/api/trips")
                    .queryParam("page", "0")
                    .queryParam("pageSize", "10")
                    .with(user(getCustomUserDetails()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.totalPages").isNumber())
                .andExpect(jsonPath("$.data.isLastPage").isBoolean())
                .andExpect(jsonPath("$.data.totalTrips").isNumber())
                .andExpect(jsonPath("$.data.trips").isArray())
                .andExpect(jsonPath("$.data.trips[0].tripId").isNumber())
                .andExpect(jsonPath("$.data.trips[0].memberId").isNumber())
                .andExpect(jsonPath("$.data.trips[0].nickname").isString())
                .andExpect(jsonPath("$.data.trips[0].tripName").isString())
                .andExpect(jsonPath("$.data.trips[0].startDate").isString())
                .andExpect(jsonPath("$.data.trips[0].endDate").isString())
                .andExpect(jsonPath("$.data.trips[0].isDomestic").isBoolean())
                .andExpect(jsonPath("$.data.trips[0].likeCount").isNumber())
                .andDo(print());
            verify(tripService, times(1)).getTrips(any(GetTripsRequestDTO.class),
                any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("getLikedTrips()은 ")
    class Context_getLikedTrips {

        @Test
        @DisplayName("회원이 좋아요 한 여행 정보 목록을 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            List<TripsResponseDTO> trips = new ArrayList<>();
            trips.add(TripsResponseDTO.builder()
                .tripId(1L)
                .memberId(1L)
                .nickname("깜찍이")
                .tripName("제주도 여행")
                .startDate("2023-10-23")
                .endDate("2023-10-27")
                .isDomestic(true)
                .likeCount(7L)
                .createdAt("2023-10-01 10:00")
                .updatedAt(null)
                .build());
            trips.add(TripsResponseDTO.builder()
                .tripId(2L)
                .memberId(2L)
                .nickname("멋쟁이")
                .tripName("속초 겨울바다 여행")
                .startDate("2023-11-27")
                .endDate("2023-11-29")
                .isDomestic(true)
                .likeCount(3L)
                .createdAt("2023-10-01 12:00")
                .updatedAt(null)
                .build());
            trips.add(TripsResponseDTO.builder()
                .tripId(3L)
                .memberId(3L)
                .nickname("꿈틀이")
                .tripName("크리스마스 미국 여행")
                .startDate("2023-12-24")
                .endDate("2023-12-26")
                .isDomestic(false)
                .likeCount(10L)
                .createdAt("2023-10-02 10:00")
                .updatedAt(null)
                .build());
            GetTripsResponseDTO getTripsResponseDTO = GetTripsResponseDTO.builder()
                .totalTrips(1)
                .isLastPage(true)
                .totalTrips(3)
                .trips(trips)
                .build();
            given(tripService.getLikedTrips(any(Long.TYPE), any(Pageable.class))).willReturn(
                getTripsResponseDTO);

            // when, then
            mockMvc.perform(get("/api/trips/likes")
                    .queryParam("page", "0")
                    .queryParam("pageSize", "10")
                    .with(user(getCustomUserDetails()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.totalPages").isNumber())
                .andExpect(jsonPath("$.data.isLastPage").isBoolean())
                .andExpect(jsonPath("$.data.totalTrips").isNumber())
                .andExpect(jsonPath("$.data.trips").isArray())
                .andExpect(jsonPath("$.data.trips[0].tripId").isNumber())
                .andExpect(jsonPath("$.data.trips[0].memberId").isNumber())
                .andExpect(jsonPath("$.data.trips[0].nickname").isString())
                .andExpect(jsonPath("$.data.trips[0].tripName").isString())
                .andExpect(jsonPath("$.data.trips[0].startDate").isString())
                .andExpect(jsonPath("$.data.trips[0].endDate").isString())
                .andExpect(jsonPath("$.data.trips[0].isDomestic").isBoolean())
                .andExpect(jsonPath("$.data.trips[0].likeCount").isNumber())
                .andDo(print());
            verify(tripService, times(1)).getLikedTrips(any(Long.TYPE), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("getTripById()는")
    class Context_getTripById {

        @Test
        @DisplayName("여행 정보를 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            List<Object> itineraries = new ArrayList<>();
            itineraries.add(GetAccommodationResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("제주 신라 호텔에서 숙박!")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn("2023-10-25 15:00")
                .checkOut("2023-10-26 11:00")
                .createdAt("2023-10-01 10:00")
                .build());
            itineraries.add(GetTransportationResponseDTO.builder()
                .itineraryId(2L)
                .itineraryName("카카오 택시타고 이동!")
                .transportation("카카오택시")
                .departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 12:00")
                .arrivalTime("2023-10-26 13:00")
                .createdAt("2023-10-01 10:00")
                .build());
            itineraries.add(GetVisitResponseDTO.builder()
                .itineraryId(3L)
                .itineraryName("카멜리아힐 구경!")
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00")
                .createdAt("2023-10-01 12:00")
                .build());
            GetTripResponseDTO trip = GetTripResponseDTO.builder()
                .tripId(1L)
                .memberId(1L)
                .nickname("깜찍이")
                .tripName("제주도 여행")
                .startDate("2023-10-23")
                .endDate("2023-10-27")
                .isDomestic(true)
                .likeCount(7L)
                .itineraries(itineraries)
                .createdAt("2023-10-02 10:00")
                .build();
            given(tripService.getTripById(any(Long.TYPE))).willReturn(trip);

            // when, then
            mockMvc.perform(get("/api/trips/{tripId}", 1L)
                    .with(user(getCustomUserDetails()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.tripId").isNumber())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.nickname").isString())
                .andExpect(jsonPath("$.data.tripName").isString())
                .andExpect(jsonPath("$.data.startDate").isString())
                .andExpect(jsonPath("$.data.endDate").isString())
                .andExpect(jsonPath("$.data.isDomestic").isBoolean())
                .andExpect(jsonPath("$.data.likeCount").isNumber())
                .andExpect(jsonPath("$.data.itineraries").isArray()).andDo(print());
            verify(tripService, times(1)).getTripById(any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("updateTrip()은")
    class Context_updateTrip {

        @Test
        @DisplayName("여행 정보를 수정할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            UpdateTripRequestDTO request = UpdateTripRequestDTO.builder()
                .tripId(1L)
                .tripName("울릉도 여행")
                .startDate("2023-10-25")
                .endDate("2023-10-26")
                .isDomestic(true)
                .build();
            TripResponseDTO trip = TripResponseDTO.builder()
                .tripId(1L)
                .memberId(1L)
                .nickname("깜찍이")
                .tripName("제주도 여행")
                .startDate("2023-10-23")
                .endDate("2023-10-27")
                .isDomestic(true)
                .likeCount(7L)
                .build();
            given(
                tripService.updateTrip(any(UpdateTripRequestDTO.class), any(Long.TYPE))).willReturn(
                trip);

            // when, then
            mockMvc.perform(patch("/api/trips")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.tripId").isNumber())
                .andExpect(jsonPath("$.data.memberId").isNumber())
                .andExpect(jsonPath("$.data.nickname").isString())
                .andExpect(jsonPath("$.data.tripName").isString())
                .andExpect(jsonPath("$.data.startDate").isString())
                .andExpect(jsonPath("$.data.endDate").isString())
                .andExpect(jsonPath("$.data.isDomestic").isBoolean())
                .andExpect(jsonPath("$.data.likeCount").isNumber())
                .andDo(print());
            verify(tripService, times(1)).updateTrip(any(UpdateTripRequestDTO.class),
                any(Long.TYPE));
        }

        @Nested
        @DisplayName("tripId가 ")
        class Element_id {

            @Test
            @DisplayName("null일 경우 여행 정보를 수정할 수 없다.")
            void null_willFail() throws Exception {
                // given
                UpdateTripRequestDTO request = UpdateTripRequestDTO.builder()
                    .tripId(null)
                    .tripName("울릉도 여행")
                    .startDate("2023-10-25")
                    .endDate("2023-10-26")
                    .isDomestic(true)
                    .build();

                // when, then
                mockMvc.perform(patch("/api/trips")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
                verify(tripService, never()).updateTrip(any(UpdateTripRequestDTO.class),
                    any(Long.TYPE));
            }
        }
    }

    @Nested
    @DisplayName("deleteTripById()는")
    class Context_deleteTripById {

        @Test
        @DisplayName("여행 정보를 삭제할 수 있다")
        void _willSuccess() throws Exception {
            //given
            List<Itinerary> itineraries = new ArrayList<>();
            itineraries.add(Accommodation.builder()
                .id(1L)
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn(LocalDateTime.of(2023, 10, 25, 15, 0))
                .checkOut(LocalDateTime.of(2023, 10, 26, 11, 0))
                .build());
            itineraries.add(
                Transportation.builder()
                    .id(2L)
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime(LocalDateTime.of(2023, 10, 26, 12, 0))
                    .arrivalTime(LocalDateTime.of(2023, 10, 26, 13, 0))
                    .build());
            itineraries.add(Visit.builder()
                .id(3L)
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .departureTime(LocalDateTime.of(2023, 10, 26, 14, 0))
                .arrivalTime(LocalDateTime.of(2023, 10, 26, 16, 0))
                .build());
            Trip trip = Trip.builder()
                .id(1L)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 23))
                .endDate(LocalDate.of(2023, 10, 27))
                .isDomestic(true)
                .itineraries(itineraries)
                .build();
            given(tripService.getTrip(any(Long.TYPE))).willReturn(trip);

            //when, then
            mockMvc.perform(delete("/api/trips/{tripId}", 1L)
                    .with(user(getCustomUserDetails()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andDo(print());
            verify(tripService, times(1)).deleteTripById(any(Long.TYPE), any(Long.TYPE));
        }
    }

    private CustomUserDetails getCustomUserDetails() {
        return new CustomUserDetails(1L, "test", "test@mail.com", "test");
    }
}
