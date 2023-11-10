package com.fc.toy_project3.domain.itinerary.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fc.toy_project3.docs.RestDocsSupport;
import com.fc.toy_project3.domain.itinerary.controller.ItineraryRestController;
import com.fc.toy_project3.domain.itinerary.dto.request.AccommodationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.AccommodationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.TransportationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.TransportationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.VisitCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.VisitUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItineraryDeleteResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItinerarySearchResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.service.ItineraryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


public class ItineraryRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    ItineraryService itineraryService;

    @Override
    public Object initController() {
        return new ItineraryRestController(itineraryService);
    }

    @Test
    @DisplayName("getPlaceByKeyword()는 query를 톻해 장소를 조회할 수 있다.")
    void getPlaceByKeyword() throws Exception {
        // given
        List<ItinerarySearchResponseDTO> itinerarySearchList = new ArrayList<>();
        itinerarySearchList.add(
            ItinerarySearchResponseDTO.builder().placeName("카카오 프렌즈 골프아카데미&스크린 파주운정점")
                .roadAddressName("경기 파주시 청암로17번길 59")
                .placeUrl("http://place.map.kakao.com/1888301706").build());
        itinerarySearchList.add(ItinerarySearchResponseDTO.builder().placeName("카카오프렌즈 스크린골프 만성점")
            .roadAddressName("전북 전주시 덕진구 만성중앙로 53-46")
            .placeUrl("http://place.map.kakao.com/1480405278").build());
        itinerarySearchList.add(ItinerarySearchResponseDTO.builder().placeName("양지카카오 프렌즈 스크린골프")
            .roadAddressName("경기 남양주시 오남읍 진건오남로830번길 14")
            .placeUrl("http://place.map.kakao.com/455587547").build());
        itinerarySearchList.add(ItinerarySearchResponseDTO.builder().placeName("카카오프렌즈스크린 금곡점")
            .roadAddressName("경기 수원시 권선구 금호로 83-8")
            .placeUrl("http://place.map.kakao.com/1052618040").build());
        given(itineraryService.getPlaceByKeyword(any())).willReturn(itinerarySearchList);

        // when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/itineraries/keyword/{query}", "카카오프렌즈"))
            .andExpect(status().isOk()).andDo(
                restDoc.document(pathParameters(parameterWithName("query").description("키워드")),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                        fieldWithPath("data[].placeName").type(JsonFieldType.STRING).description("장소명"),
                        fieldWithPath("data[].roadAddressName").type(JsonFieldType.STRING)
                            .description("장소 도로명"),
                        fieldWithPath("data[].placeUrl").type(JsonFieldType.STRING)
                            .description("장소 url"))));
    }

    @Test
    @DisplayName("숙박 여정 정보를 저장할 수 있다.")
    void createAccommodation() throws Exception {
        // given
        AccommodationCreateRequestDTO request = AccommodationCreateRequestDTO.builder().tripId(1L)
            .itineraryName("제주여정1").accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75").checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00").build();
        AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
            .itineraryId(1L).itineraryName("제주여정1").accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75").checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00").build();

        given(itineraryService.createAccommodation(
            any(AccommodationCreateRequestDTO.class))).willReturn(accommodationResponseDTO);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/itineraries/accommodations")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(restDoc.document(
                requestFields(fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("accommodationName").type(JsonFieldType.STRING).description("숙소명"),
                    fieldWithPath("accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명"),
                    fieldWithPath("checkIn").type(JsonFieldType.STRING).description("체크인 일시"),
                    fieldWithPath("checkOut").type(JsonFieldType.STRING).description("체크아웃 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING)
                        .description("숙소명"),
                    fieldWithPath("data.accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명"),
                    fieldWithPath("data.checkIn").type(JsonFieldType.STRING).description("체크인 일시"),
                    fieldWithPath("data.checkOut").type(JsonFieldType.STRING).description("체크아웃 일시"))));
    }

    @Test
    @DisplayName("이동 여정 정보를 저장할 수 있다.")
    void createTransportation() throws Exception {
        // given
        TransportationCreateRequestDTO request = TransportationCreateRequestDTO.builder().tripId(1L)
            .itineraryName("제주여정2").transportation("카카오택시").departurePlace("제주신라호텔")
            .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75").destination("오설록 티 뮤지엄")
            .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
            .departureTime("2023-10-26 12:00").arrivalTime("2023-10-26 13:00").build();
        TransportationResponseDTO transportationResponseDTO = TransportationResponseDTO.builder()
            .itineraryId(2L).itineraryName("제주여정2").transportation("카카오택시").departurePlace("제주신라호텔")
            .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75").destination("오설록 티 뮤지엄")
            .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
            .departureTime("2023-10-26 12:00").arrivalTime("2023-10-26 13:00").build();
        given(itineraryService.createTransportation(
            any(TransportationCreateRequestDTO.class))).willReturn(transportationResponseDTO);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/itineraries/transportations")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(restDoc.document(
                requestFields(fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("transportation").type(JsonFieldType.STRING).description("이동 수단"),
                    fieldWithPath("departurePlace").type(JsonFieldType.STRING).description("출발지"),
                    fieldWithPath("departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명"),
                    fieldWithPath("destination").type(JsonFieldType.STRING).description("도착지"),
                    fieldWithPath("destinationRoadAddressName").type(JsonFieldType.STRING)
                        .description("도착지 도로명"),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).description("출발 일시"),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).description("도착 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("data.transportation").type(JsonFieldType.STRING)
                        .description("이동 수단"),
                    fieldWithPath("data.departurePlace").type(JsonFieldType.STRING).description("출발지"),
                    fieldWithPath("data.departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명"),
                    fieldWithPath("data.destination").type(JsonFieldType.STRING).description("도착지"),
                    fieldWithPath("data.destinationRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("도착지 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING).description("출발 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("도착 일시"))));
    }

    @Test
    @DisplayName("체류 여정 정보를 저장할 수 있다.")
    void createVisit() throws Exception {
        // given
        VisitCreateRequestDTO request = VisitCreateRequestDTO.builder().tripId(1L)
            .itineraryName("제주여정3").placeName("카멜리아힐").placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
            .arrivalTime("2023-10-26 14:00").departureTime("2023-10-26 16:00").build();
        VisitResponseDTO visitResponseDTO = VisitResponseDTO.builder().itineraryId(1L)
            .itineraryName("제주여정3").placeName("카멜리아힐").placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
            .arrivalTime("2023-10-26 14:00").departureTime("2023-10-26 16:00").build();
        given(itineraryService.createVisit(any(VisitCreateRequestDTO.class))).willReturn(
            visitResponseDTO);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/itineraries/visits")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(restDoc.document(
                requestFields(fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("placeRoadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명"),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).description("도착 일시"),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).description("출발 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("data.placeRoadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING).description("도착 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("출발 일시"))));
    }

    @Test
    @DisplayName("getItineraryByTripId()는 tripId를 통해 Itinerary를 조회할 수 있다.")
    void getItineraryByTripId() throws Exception {
        // given
        List<Object> itinerarys = new ArrayList<>();
        itinerarys.add(AccommodationResponseDTO.builder().itineraryId(1L).itineraryName("제주여정1")
            .accommodationName("제주신라호텔").accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .checkIn("2023-10-25 15:00").checkOut("2023-10-26 11:00").build());
        itinerarys.add(TransportationResponseDTO.builder().itineraryId(2L).itineraryName("제주여정2")
            .transportation("카카오택시").departurePlace("제주신라호텔")
            .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75").destination("오설록 티 뮤지엄")
            .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
            .departureTime("2023-10-26 12:00").arrivalTime("2023-10-26 13:00").build());
        itinerarys.add(
            VisitResponseDTO.builder().itineraryId(3L).itineraryName("제주여정3").placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166").arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00").build());

        given(itineraryService.getItineraryByTripId(any(Long.TYPE))).willReturn(itinerarys);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/itineraries/{tripId}", 1L))
            .andExpect(status().isOk()).andDo(
                restDoc.document(pathParameters(parameterWithName("tripId").description("여행 식별자")),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                        fieldWithPath("data[].itineraryId").type(JsonFieldType.NUMBER).optional()
                            .description("여정 식별자"),
                        fieldWithPath("data[].itineraryName").type(JsonFieldType.STRING).optional()
                            .description("여정 이름"),
                        fieldWithPath("data[].accommodationName").type(JsonFieldType.STRING).optional()
                            .description("숙소명"),
                        fieldWithPath("data[].accommodationRoadAddressName").type(JsonFieldType.STRING)
                            .optional().description("숙소 도로명"),
                        fieldWithPath("data[].checkIn").type(JsonFieldType.STRING).optional()
                            .description("체크인 일시"),
                        fieldWithPath("data[].checkOut").type(JsonFieldType.STRING).optional()
                            .description("체크아웃 일시"),
                        fieldWithPath("data[].transportation").type(JsonFieldType.STRING).optional()
                            .description("이동 수단"),
                        fieldWithPath("data[].departurePlace").type(JsonFieldType.STRING).optional()
                            .description("출발지"),
                        fieldWithPath("data[].departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                            .optional().description("출발지 도로명"),
                        fieldWithPath("data[].destination").type(JsonFieldType.STRING).optional()
                            .description("도착지"),
                        fieldWithPath("data[].destinationRoadAddressName").type(JsonFieldType.STRING)
                            .optional().description("도착지 도로명"),
                        fieldWithPath("data[].departureTime").type(JsonFieldType.STRING).optional()
                            .description("출발 일시"),
                        fieldWithPath("data[].arrivalTime").type(JsonFieldType.STRING).optional()
                            .description("도착 일시"),
                        fieldWithPath("data[].placeName").type(JsonFieldType.STRING).optional()
                            .description("장소명"),
                        fieldWithPath("data[].placeRoadAddressName").type(JsonFieldType.STRING)
                            .optional().description("장소 도로명"),
                        fieldWithPath("data[].departureTime").type(JsonFieldType.STRING).optional()
                            .description("도착 일시"),
                        fieldWithPath("data[].arrivalTime").type(JsonFieldType.STRING).optional()
                            .description("출발 일시"))));

    }

    @Test
    @DisplayName("숙박 여정 정보를 수정할 수 있다.")
    void updateAccommodation() throws Exception {
        // given
        AccommodationUpdateRequestDTO request = AccommodationUpdateRequestDTO.builder()
            .itineraryId(1L).itineraryName("즐거운 제주여정1").accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75").checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00").build();
        AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
            .itineraryId(1L).itineraryName("즐거운 제주여정1").accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75").checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00:00").build();
        given(itineraryService.updateAccommodation(
            any(AccommodationUpdateRequestDTO.class))).willReturn(accommodationResponseDTO);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/itineraries/accommodations")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk()).andDo(restDoc.document(requestFields(
                    fieldWithPath("itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("accommodationName").type(JsonFieldType.STRING).description("숙소명"),
                    fieldWithPath("accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명"),
                    fieldWithPath("checkIn").type(JsonFieldType.STRING).description("체크인 일시"),
                    fieldWithPath("checkOut").type(JsonFieldType.STRING).description("체크아웃 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING)
                        .description("숙소명"),
                    fieldWithPath("data.accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명"),
                    fieldWithPath("data.checkIn").type(JsonFieldType.STRING).description("체크인 일시"),
                    fieldWithPath("data.checkOut").type(JsonFieldType.STRING).description("체크아웃 일시"))));
    }

    @Test
    @DisplayName("이동 여정 정보를 수정할 수 있다.")
    void updateTransportation() throws Exception {
        // given
        TransportationUpdateRequestDTO request = TransportationUpdateRequestDTO.builder()
            .itineraryId(1L).itineraryId(1L).itineraryName("즐거운 제주여정2").transportation("카카오택시")
            .departurePlace("제주신라호텔").departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .destination("오설록 티 뮤지엄").destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
            .departureTime("2023-10-26 11:00").arrivalTime("2023-10-26 13:00").build();
        TransportationResponseDTO transportationResponseDTO = TransportationResponseDTO.builder()
            .itineraryId(1L).itineraryName("즐거운 제주여정2").transportation("카카오택시")
            .departurePlace("제주신라호텔").departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .destination("오설록 티 뮤지엄").destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
            .departureTime("2023-10-26 11:00").arrivalTime("2023-10-26 13:00").build();
        given(itineraryService.updateTransportation(
            any(TransportationUpdateRequestDTO.class))).willReturn(transportationResponseDTO);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/itineraries/transportations")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk()).andDo(restDoc.document(requestFields(
                    fieldWithPath("itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("transportation").type(JsonFieldType.STRING).description("이동 수단"),
                    fieldWithPath("departurePlace").type(JsonFieldType.STRING).description("출발지"),
                    fieldWithPath("departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명"),
                    fieldWithPath("destination").type(JsonFieldType.STRING).description("도착지"),
                    fieldWithPath("destinationRoadAddressName").type(JsonFieldType.STRING)
                        .description("도착지 도로명"),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).description("출발 일시"),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).description("도착 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("data.transportation").type(JsonFieldType.STRING)
                        .description("이동 수단"),
                    fieldWithPath("data.departurePlace").type(JsonFieldType.STRING).description("출발지"),
                    fieldWithPath("data.departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명"),
                    fieldWithPath("data.destination").type(JsonFieldType.STRING).description("도착지"),
                    fieldWithPath("data.destinationRoadAddressName").type(JsonFieldType.STRING)
                        .description("도착지 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING).description("출발 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("도착 일시"))));
    }

    @Test
    @DisplayName("체류 여정 정보를 수정할 수 있다.")
    void updateVisit() throws Exception {
        VisitUpdateRequestDTO request = VisitUpdateRequestDTO.builder().itineraryId(1L)
            .itineraryName("즐거운 제주여정3").placeName("카멜리아힐")
            .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166").arrivalTime("2023-10-26 14:00")
            .departureTime("2023-10-26 16:00").build();
        VisitResponseDTO visitResponseDTO = VisitResponseDTO.builder().itineraryId(1L)
            .itineraryName("즐거운 제주여정3").placeName("카멜리아힐")
            .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166").arrivalTime("2023-10-26 14:00")
            .departureTime("2023-10-26 16:00").build();
        given(itineraryService.updateVisit(any(VisitUpdateRequestDTO.class))).willReturn(
            visitResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/itineraries/visits")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk()).andDo(restDoc.document(requestFields(
                    fieldWithPath("itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("placeRoadAddressName").type(JsonFieldType.STRING).description("장소 도로명"),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).description("도착 일시"),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).description("출발 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING).description("여정 이름"),
                    fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("data.placeRoadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING).description("도착 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("출발 일시"))));
    }

    @Test
    @DisplayName("deleteItinerary()는 여정 정보를 삭제할 수 있다.")
    void deleteItinerary() throws Exception {
        // given
        ItineraryDeleteResponseDTO itinerary = ItineraryDeleteResponseDTO.builder().itineraryId(1L)
            .build();
        given(itineraryService.deleteItinerary(any(Long.TYPE))).willReturn(itinerary);

        // when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/itineraries/{itineraryId}", 1L))
            .andExpect(status().isOk()).andDo(
                restDoc.document(pathParameters(parameterWithName("itineraryId").description("여정 식별자")),
                    responseFields(responseCommon()).and(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                            .description("여정 식별자"))));
    }
}
