package com.fc.toy_project3.domain.itinerary.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import com.fc.toy_project3.domain.itinerary.controller.ItineraryRestController;
import com.fc.toy_project3.domain.itinerary.dto.request.create.AccommodationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.create.TransportationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.create.VisitCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.AccommodationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.TransportationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.VisitUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItinerarySearchResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.service.ItineraryService;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItineraryRestController.class)
public class ItineraryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItineraryService itineraryService;

    @MockBean
    MemberService memberService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("getPlaceByKeyword()는")
    class Context_getPlaceByKeyword {

        @Test
        @DisplayName("query를 톻해 장소를 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            List<ItinerarySearchResponseDTO> itinerarySearchList = new ArrayList<>();
            itinerarySearchList.add(
                ItinerarySearchResponseDTO.builder()
                    .placeName("카카오 프렌즈 골프아카데미&스크린 파주운정점")
                    .roadAddressName("경기 파주시 청암로17번길 59")
                    .placeUrl("http://place.map.kakao.com/1888301706")
                    .build());
            itinerarySearchList.add(
                ItinerarySearchResponseDTO.builder()
                    .placeName("카카오프렌즈 스크린골프 만성점")
                    .roadAddressName("전북 전주시 덕진구 만성중앙로 53-46")
                    .placeUrl("http://place.map.kakao.com/1480405278")
                    .build());
            itinerarySearchList.add(
                ItinerarySearchResponseDTO.builder()
                    .placeName("양지카카오 프렌즈 스크린골프")
                    .roadAddressName("경기 남양주시 오남읍 진건오남로830번길 14")
                    .placeUrl("http://place.map.kakao.com/455587547")
                    .build());
            itinerarySearchList.add(ItinerarySearchResponseDTO.builder()
                .placeName("카카오프렌즈스크린 금곡점")
                .roadAddressName("경기 수원시 권선구 금호로 83-8")
                .placeUrl("http://place.map.kakao.com/1052618040")
                .build());
            given(itineraryService.getPlaceByKeyword(any())).willReturn(itinerarySearchList);

            // when, then
            mockMvc.perform(get("/api/itineraries/keyword/{query}", "카카오프렌즈")
                    .with(user(getCustomUserDetails()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].placeName").isString())
                .andExpect(jsonPath("$.data[0].roadAddressName").isString())
                .andExpect(jsonPath("$.data[0].placeUrl").exists()).andDo(print());
        }
    }

    @Nested
    @DisplayName("createAccommodation()은")
    class Context_createAccommodation {

        @Test
        @DisplayName("숙박 여정 정보를 저장할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                .tripId(1L)
                .itineraryName("제주여정1")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn("2023-10-25 15:00")
                .checkOut("2023-10-26 11:00")
                .build();
            AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("제주여정1")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn("2023-10-25 15:00:00")
                .checkOut("2023-10-26 11:00:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.createAccommodation(any(AccommodationCreateRequestDTO.class),
                any(Long.TYPE))).willReturn(accommodationResponseDTO);

            // when, then
            mockMvc.perform(post("/api/itineraries/accommodations")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.accommodationName").isString())
                .andExpect(jsonPath("$.data.accommodationRoadAddressName").isString())
                .andExpect(jsonPath("$.data.checkIn").isString())
                .andExpect(jsonPath("$.data.checkOut").isString())
                .andDo(print());
        }

        @Nested
        @DisplayName("tripId가 ")
        class Element_tripId {

            @Test
            @DisplayName("null 이면 숙박 여정 정보를 저장할 수 없다.")
            void null_willFail() throws Exception {
                // given
                AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                    .tripId(null)
                    .itineraryName("제주여정1")
                    .accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn("2023-10-25 15:00")
                    .checkOut("2023-10-26 11:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("itineraryName이 ")
        class Element_itineraryName {

            @Test
            @DisplayName("blank 이면 숙박 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName(" ")
                    .accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn("2023-10-25 15:00")
                    .checkOut("2023-10-26 11:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("accommodationName이 ")
        class Element_accommodationName {

            @Test
            @DisplayName("blank 이면 숙박 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정1")
                    .accommodationName(" ")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn("2023-10-25 15:00")
                    .checkOut("2023-10-26 11:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("accommodationRoadAddressName이 ")
        class Element_accommodationRoadAddressName {

            @Test
            @DisplayName("blank 이면 숙박 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정1")
                    .accommodationName("제주신라호텔")
                    .accommodationRoadAddressName(" ")
                    .checkIn("2023-10-25 15:00")
                    .checkOut("2023-10-26 11:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("checkIn이 ")
        class Element_checkIn {

            @Test
            @DisplayName("blank 이면 숙박 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정1")
                    .accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn(" ")
                    .checkOut("2023-10-26 11:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("checkOut이 ")
        class Element_checkOut {

            @Test
            @DisplayName("blank 이면 숙박 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정1")
                    .accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn("2023-10-25 15:00")
                    .checkOut(" ")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("createTransportation()은")
    @WithMockUser
    class Context_createTransportation {

        @Test
        @DisplayName("이동 여정 정보를 저장할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                .tripId(1L)
                .itineraryName("제주여정2")
                .transportation("카카오택시")
                .departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 12:00")
                .arrivalTime("2023-10-26 13:00").build();
            TransportationResponseDTO transportationResponseDTO = TransportationResponseDTO.builder()
                .itineraryId(2L)
                .itineraryName("제주여정2")
                .transportation("카카오택시")
                .departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 12:00")
                .arrivalTime("2023-10-26 13:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.createTransportation(any(TransportationCreateRequestDTO.class),
                any(Long.TYPE))).willReturn(transportationResponseDTO);

            // when, then
            mockMvc.perform(post("/api/itineraries/transportations")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.transportation").isString())
                .andExpect(jsonPath("$.data.departurePlace").isString())
                .andExpect(jsonPath("$.data.departurePlaceRoadAddressName").isString())
                .andExpect(jsonPath("$.data.destination").isString())
                .andExpect(jsonPath("$.data.destinationRoadAddressName").isString())
                .andExpect(jsonPath("$.data.departureTime").isString())
                .andExpect(jsonPath("$.data.arrivalTime").isString())
                .andDo(print());
        }

        @Nested
        @DisplayName("tripId가 ")
        class Element_tripId {

            @Test
            @DisplayName("null 이면 이동 여정 정보를 저장할 수 없다.")
            void null_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(null)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("itineraryName이 ")
        class Element_itineraryName {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName(" ")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("transportation이 ")
        class Element_transportation {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation(" ")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("departurePlace가 ")
        class Element_departurePlace {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace(" ")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("departurePlaceRoadAddressName이 ")
        class Element_departurePlaceRoadAddressName {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName(" ")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("destination이 ")
        class Element_destination {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination(" ")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("destinationRoadAddressName이 ")
        class Element_destinationRoadAddressName {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName(" ")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("departureTime이 ")
        class Element_departureTime {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime(" ")
                    .arrivalTime("2023-10-26 13:00").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("arrivalTime이 ")
        class Element_arrivalTime {

            @Test
            @DisplayName("blank 이면 이동 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 12:00")
                    .arrivalTime(" ").build();

                // when, then
                mockMvc.perform(post("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("createVisit()은")
    @WithMockUser
    class Context_createVisit {

        @Test
        @DisplayName("체류 여정 정보를 저장할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                .tripId(1L)
                .itineraryName("제주여정3")
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00")
                .build();
            VisitResponseDTO visitResponseDTO = VisitResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("제주여정3")
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.createVisit(any(VisitCreateRequestDTO.class),
                any(Long.TYPE))).willReturn(
                visitResponseDTO);

            // when, then
            mockMvc.perform(post("/api/itineraries/visits")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.placeName").isString())
                .andExpect(jsonPath("$.data.placeRoadAddressName").isString())
                .andExpect(jsonPath("$.data.departureTime").isString())
                .andExpect(jsonPath("$.data.arrivalTime").isString())
                .andDo(print());
        }

        @Nested
        @DisplayName("tripId가 ")
        class Element_tripId {

            @Test
            @DisplayName("null 이면 체류 여정 정보를 저장할 수 없다.")
            void null_willFail() throws Exception {
                // given
                VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                    .tripId(null)
                    .itineraryName("제주여정3")
                    .placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime("2023-10-26 14:00")
                    .departureTime("2023-10-26 16:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("itineraryName이 ")
        class Element_itineraryName {

            @Test
            @DisplayName("blank 이면 체류 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName(" ")
                    .placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime("2023-10-26 14:00")
                    .departureTime("2023-10-26 16:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("placeName이 ")
        class Element_placeName {

            @Test
            @DisplayName("blank 이면 체류 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정3")
                    .placeName(" ")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime("2023-10-26 14:00")
                    .departureTime("2023-10-26 16:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("placeRoadAddressName이 ")
        class Element_placeRoadAddressName {

            @Test
            @DisplayName("blank 이면 체류 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정3")
                    .placeName("카멜리아힐")
                    .placeRoadAddressName(" ")
                    .arrivalTime("2023-10-26 14:00")
                    .departureTime("2023-10-26 16:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("arrivalTime이 ")
        class Element_arrivalTime {

            @Test
            @DisplayName("blank 이면 체류 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                    .tripId(null)
                    .itineraryName("제주여정3")
                    .placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime(" ")
                    .departureTime("2023-10-26 16:00")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }

        @Nested
        @DisplayName("departureTime이 ")
        class Element_departureTime {

            @Test
            @DisplayName("blank 이면 체류 여정 정보를 저장할 수 없다.")
            void blank_willFail() throws Exception {
                // given
                VisitCreateRequestDTO request = VisitCreateRequestDTO.builder()
                    .tripId(1L)
                    .itineraryName("제주여정3")
                    .placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime("2023-10-26 14:00")
                    .departureTime(" ")
                    .build();

                // when, then
                mockMvc.perform(post("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("updateAccommodation()은")
    @WithMockUser
    class Context_updateAccommodation {

        @Test
        @DisplayName("숙박 여정 정보를 수정할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            AccommodationUpdateRequestDTO request = AccommodationUpdateRequestDTO.builder()
                .itineraryId(1L)
                .itineraryName("즐거운 제주여정1")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn("2023-10-25 15:00")
                .checkOut("2023-10-26 11:00")
                .build();
            AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("즐거운 제주여정1")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn("2023-10-25 15:00")
                .checkOut("2023-10-26 11:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.updateAccommodation(any(AccommodationUpdateRequestDTO.class),
                any(Long.TYPE))).willReturn(accommodationResponseDTO);

            // when, then
            mockMvc.perform(patch("/api/itineraries/accommodations")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.accommodationName").isString())
                .andExpect(jsonPath("$.data.accommodationRoadAddressName").isString())
                .andExpect(jsonPath("$.data.checkIn").isString())
                .andExpect(jsonPath("$.data.checkOut").isString())
                .andDo(print());
        }

        @Nested
        @DisplayName("itineraryId가 ")
        class Element_itineraryId {

            @Test
            @DisplayName("null 이면 숙박 여정 정보를 수정할 수 없다.")
            void null_willFail() throws Exception {
                // given
                AccommodationUpdateRequestDTO request = AccommodationUpdateRequestDTO.builder()
                    .itineraryId(null)
                    .itineraryName("즐거운 제주여정1")
                    .accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn("2023-10-25 15:00")
                    .checkOut("2023-10-26 11:00")
                    .build();

                // when, then
                mockMvc.perform(patch("/api/itineraries/accommodations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("updateTransportation()은")
    @WithMockUser
    class Context_updateTransportation {

        @Test
        @DisplayName("이동 여정 정보를 수정할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            TransportationUpdateRequestDTO request = TransportationUpdateRequestDTO.builder()
                .itineraryId(1L)
                .itineraryName("즐거운 제주여정2")
                .transportation("카카오택시")
                .departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 11:00")
                .arrivalTime("2023-10-26 13:00")
                .build();
            TransportationResponseDTO transportationResponseDTO = TransportationResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("즐거운 제주여정2")
                .transportation("카카오택시")
                .departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 11:00")
                .arrivalTime("2023-10-26 13:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.updateTransportation(any(TransportationUpdateRequestDTO.class),
                any(Long.TYPE))).willReturn(transportationResponseDTO);

            // when, then
            mockMvc.perform(patch("/api/itineraries/transportations")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.transportation").isString())
                .andExpect(jsonPath("$.data.departurePlace").isString())
                .andExpect(jsonPath("$.data.departurePlaceRoadAddressName").isString())
                .andExpect(jsonPath("$.data.destination").isString())
                .andExpect(jsonPath("$.data.destinationRoadAddressName").isString())
                .andExpect(jsonPath("$.data.departureTime").isString())
                .andExpect(jsonPath("$.data.arrivalTime").isString())
                .andDo(print());
        }

        @Nested
        @DisplayName("itineraryId가 ")
        class Element_itineraryId {

            @Test
            @DisplayName("null 이면 이동 여정 정보를 수정할 수 없다.")
            void null_willFail() throws Exception {
                // given
                TransportationUpdateRequestDTO request = TransportationUpdateRequestDTO.builder()
                    .itineraryId(null)
                    .itineraryName("즐거운 제주여정2")
                    .transportation("카카오택시")
                    .departurePlace("제주신라호텔")
                    .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄")
                    .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime("2023-10-26 11:00")
                    .arrivalTime("2023-10-26 13:00")
                    .build();

                // when, then
                mockMvc.perform(patch("/api/itineraries/transportations")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("updateVisit()은")
    @WithMockUser
    class Context_updateVisit {

        @Test
        @DisplayName("체류 여정 정보를 수정할 수 있다.")
        void _willSuccess() throws Exception {
            VisitUpdateRequestDTO request = VisitUpdateRequestDTO.builder()
                .itineraryId(1L)
                .itineraryName("즐거운 제주여정3")
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00")
                .build();
            VisitResponseDTO visitResponseDTO = VisitResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("즐거운 제주여정3")
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.updateVisit(any(VisitUpdateRequestDTO.class),
                any(Long.TYPE))).willReturn(visitResponseDTO);

            mockMvc.perform(patch("/api/itineraries/visits")
                    .with(user(getCustomUserDetails()))
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.placeName").isString())
                .andExpect(jsonPath("$.data.placeRoadAddressName").isString())
                .andExpect(jsonPath("$.data.departureTime").isString())
                .andExpect(jsonPath("$.data.arrivalTime").isString())
                .andDo(print());
        }

        @Nested
        @DisplayName("itineraryId가 ")
        class Element_itineraryId {

            @Test
            @DisplayName("null 이면 체류 여정 정보를 수정할 수 없다.")
            void null_willFail() throws Exception {
                // given
                VisitUpdateRequestDTO request = VisitUpdateRequestDTO.builder()
                    .itineraryId(null)
                    .itineraryName("즐거운 제주여정3")
                    .placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime("2023-10-26 14:00")
                    .departureTime("2023-10-26 16:00")
                    .build();

                // when, then
                mockMvc.perform(patch("/api/itineraries/visits")
                        .with(user(getCustomUserDetails()))
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("deleteItinerary()는")
    @WithMockUser
    class Context_deleteItinerary {

        @Test
        @DisplayName("여정 정보를 삭제할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
                .itineraryId(1L)
                .itineraryName("제주여정1")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn("2023-10-25 15:00:00")
                .checkOut("2023-10-26 11:00:00")
                .build();
            given(memberService.getMember(any(Long.TYPE))).willReturn(getMember());
            given(itineraryService.deleteItinerary(any(Long.TYPE), any(Long.TYPE))).willReturn(
                accommodationResponseDTO);

            // when, then
            mockMvc.perform(delete("/api/itineraries/{itineraryId}", 1L)
                    .with(user(getCustomUserDetails()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.itineraryId").isNumber())
                .andExpect(jsonPath("$.data.itineraryName").isString())
                .andExpect(jsonPath("$.data.accommodationName").isString())
                .andExpect(jsonPath("$.data.accommodationRoadAddressName").isString())
                .andExpect(jsonPath("$.data.checkIn").isString())
                .andExpect(jsonPath("$.data.checkOut").isString())
                .andDo(print());
        }
    }

    private CustomUserDetails getCustomUserDetails() {
        return new CustomUserDetails(1L, "test", "test@mail.com", "test");
    }

    private Member getMember() {
        return Member.builder()
            .id(1L)
            .email("test@gmail.com")
            .password("qwer1234$$")
            .name("test")
            .nickname("test")
            .build();
    }
}
