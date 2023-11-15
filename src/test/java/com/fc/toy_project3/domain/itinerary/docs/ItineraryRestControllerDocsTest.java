package com.fc.toy_project3.domain.itinerary.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fc.toy_project3.docs.RestDocsSupport;
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
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ItineraryRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    ItineraryService itineraryService;

    @MockBean
    MemberService memberService;

    @Override
    public Object initController() {
        return new ItineraryRestController(itineraryService, memberService);
    }

    private final ConstraintDescriptions createAccommodationConstraints = new ConstraintDescriptions(
        AccommodationCreateRequestDTO.class);
    private final ConstraintDescriptions createTransportationConstraints = new ConstraintDescriptions(
        TransportationCreateRequestDTO.class);
    private final ConstraintDescriptions createVisitConstraints = new ConstraintDescriptions(
        VisitCreateRequestDTO.class);
    private final ConstraintDescriptions updateAccommodationConstraints = new ConstraintDescriptions(
        AccommodationUpdateRequestDTO.class);
    private final ConstraintDescriptions updateTransportationConstraints = new ConstraintDescriptions(
        TransportationUpdateRequestDTO.class);
    private final ConstraintDescriptions updateVisitConstraints = new ConstraintDescriptions(
        VisitUpdateRequestDTO.class);

    @Test
    @DisplayName("getPlaceByKeyword()는 query를 톻해 장소를 조회할 수 있다.")
    void getPlaceByKeyword() throws Exception {
        // given
        List<ItinerarySearchResponseDTO> itinerarySearchList = new ArrayList<>();
        itinerarySearchList.add(ItinerarySearchResponseDTO.builder()
            .placeName("카카오 프렌즈 골프아카데미&스크린 파주운정점")
            .roadAddressName("경기 파주시 청암로17번길 59")
            .placeUrl("http://place.map.kakao.com/1888301706")
            .build());
        itinerarySearchList.add(ItinerarySearchResponseDTO.builder()
            .placeName("카카오프렌즈 스크린골프 만성점")
            .roadAddressName("전북 전주시 덕진구 만성중앙로 53-46")
            .placeUrl("http://place.map.kakao.com/1480405278")
            .build());
        itinerarySearchList.add(ItinerarySearchResponseDTO.builder()
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
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/itineraries/keyword/{query}", "카카오프렌즈")
                .with(user(customUserDetails))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                pathParameters(parameterWithName("query").description("키워드")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                    fieldWithPath("data[].placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("data[].roadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명"),
                    fieldWithPath("data[].placeUrl").type(JsonFieldType.STRING)
                        .description("장소 url")
                )
            ));
    }

    @Test
    @DisplayName("숙박 여정 정보를 저장할 수 있다.")
    @WithMockUser
    void createAccommodation() throws Exception {
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
            .checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00")
            .build();
        given(itineraryService.createAccommodation(
            any(AccommodationCreateRequestDTO.class), any(Long.TYPE))).willReturn(
            accommodationResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/itineraries/accommodations")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자")
                        .attributes(key("constraints")
                            .value(
                                createAccommodationConstraints.descriptionsForProperty("tripId"))),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름")
                        .attributes(key("constraints")
                            .value(createAccommodationConstraints
                                .descriptionsForProperty("itineraryName"))),
                    fieldWithPath("accommodationName").type(JsonFieldType.STRING)
                        .description("숙소명").attributes(key("constraints")
                            .value(createAccommodationConstraints
                                .descriptionsForProperty("accommodationName"))),
                    fieldWithPath("accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명").attributes(key("constraints")
                            .value(createAccommodationConstraints
                                .descriptionsForProperty("accommodationRoadAddressName"))),
                    fieldWithPath("checkIn").type(JsonFieldType.STRING).description("체크인 일시")
                        .description("숙소 도로명").attributes(key("constraints")
                            .value(createAccommodationConstraints
                                .descriptionsForProperty("checkIn"))),
                    fieldWithPath("checkOut").type(JsonFieldType.STRING).description("체크아웃 일시")
                        .description("숙소 도로명").attributes(key("constraints").value(
                            createAccommodationConstraints.descriptionsForProperty("checkOut")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING)
                        .description("숙소명"),
                    fieldWithPath("data.accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명"),
                    fieldWithPath("data.checkIn").type(JsonFieldType.STRING).description("체크인 일시"),
                    fieldWithPath("data.checkOut").type(JsonFieldType.STRING)
                        .description("체크아웃 일시")
                )
            ));
    }

    @Test
    @DisplayName("이동 여정 정보를 저장할 수 있다.")
    void createTransportation() throws Exception {
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
            .arrivalTime("2023-10-26 13:00")
            .build();
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
        given(itineraryService.createTransportation(any(TransportationCreateRequestDTO.class),
            any(Long.TYPE)))
            .willReturn(transportationResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/itineraries/transportations")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자")
                        .attributes(key("constraints")
                            .value(
                                createTransportationConstraints.descriptionsForProperty("tripId"))),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름")
                        .attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("itineraryName"))),
                    fieldWithPath("transportation").type(JsonFieldType.STRING).description("이동 수단")
                        .attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("transportation"))),
                    fieldWithPath("departurePlace").type(JsonFieldType.STRING).description("출발지")
                        .attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("departurePlace"))),
                    fieldWithPath("departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명").attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("departurePlaceRoadAddressName"))),
                    fieldWithPath("destination").type(JsonFieldType.STRING).description("도착지")
                        .attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("destination"))),
                    fieldWithPath("destinationRoadAddressName").type(JsonFieldType.STRING)
                        .description("도착지 도로명").attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("destinationRoadAddressName"))),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).description("출발 일시")
                        .attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("departureTime"))),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).description("도착 일시")
                        .attributes(key("constraints")
                            .value(createTransportationConstraints
                                .descriptionsForProperty("arrivalTime")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여행 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.transportation").type(JsonFieldType.STRING)
                        .description("이동 수단"),
                    fieldWithPath("data.departurePlace").type(JsonFieldType.STRING)
                        .description("출발지"),
                    fieldWithPath("data.departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명"),
                    fieldWithPath("data.destination").type(JsonFieldType.STRING).description("도착지"),
                    fieldWithPath("data.destinationRoadAddressName").type(JsonFieldType.STRING)
                        .description("도착지 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING)
                        .description("출발 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("도착 일시")
                )
            ));
    }

    @Test
    @DisplayName("체류 여정 정보를 저장할 수 있다.")
    void createVisit() throws Exception {
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
        given(itineraryService.createVisit(any(VisitCreateRequestDTO.class), any(Long.TYPE)))
            .willReturn(visitResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/itineraries/visits")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자")
                        .attributes(key("constraints")
                            .value(createVisitConstraints.descriptionsForProperty("tripId"))),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).description("여정 이름")
                        .attributes(key("constraints")
                            .value(createVisitConstraints
                                .descriptionsForProperty("itineraryName"))),
                    fieldWithPath("placeName").type(JsonFieldType.STRING).description("장소명")
                        .attributes(key("constraints")
                            .value(createVisitConstraints
                                .descriptionsForProperty("placeName"))),
                    fieldWithPath("placeRoadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명").attributes(key("constraints")
                            .value(createVisitConstraints
                                .descriptionsForProperty("placeRoadAddressName"))),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).description("도착 일시")
                        .attributes(key("constraints")
                            .value(createVisitConstraints
                                .descriptionsForProperty("departureTime"))),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).description("출발 일시")
                        .attributes(key("constraints")
                            .value(createVisitConstraints
                                .descriptionsForProperty("arrivalTime")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("data.placeRoadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING)
                        .description("도착 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("출발 일시")
                )
            ));
    }

    @Test
    @DisplayName("숙박 여정 정보를 수정할 수 있다.")
    void updateAccommodation() throws Exception {
        // given
        AccommodationUpdateRequestDTO request = AccommodationUpdateRequestDTO.builder()
            .itineraryId(1L)
            .itineraryName("즐거운 제주여정1")
            .build();
        AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
            .itineraryId(1L)
            .itineraryName("즐거운 제주여정1")
            .accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00")
            .build();
        given(itineraryService.updateAccommodation(any(AccommodationUpdateRequestDTO.class),
            any(Long.TYPE))).willReturn(accommodationResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/itineraries/accommodations")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자")
                        .attributes(key("constraints")
                            .value(updateAccommodationConstraints.descriptionsForProperty(
                                "itineraryId"))),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).optional()
                        .description("여정 이름"),
                    fieldWithPath("accommodationName").type(JsonFieldType.STRING).optional()
                        .description("숙소명"),
                    fieldWithPath("accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("숙소 도로명"),
                    fieldWithPath("checkIn").type(JsonFieldType.STRING).optional()
                        .description("체크인 일시"),
                    fieldWithPath("checkOut").type(JsonFieldType.STRING).optional()
                        .description("체크아웃 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING)
                        .description("숙소명"),
                    fieldWithPath("data.accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .description("숙소 도로명"),
                    fieldWithPath("data.checkIn").type(JsonFieldType.STRING).description("체크인 일시"),
                    fieldWithPath("data.checkOut").type(JsonFieldType.STRING)
                        .description("체크아웃 일시")
                )
            ));
    }

    @Test
    @DisplayName("이동 여정 정보를 수정할 수 있다.")
    void updateTransportation() throws Exception {
        // given
        TransportationUpdateRequestDTO request = TransportationUpdateRequestDTO.builder()
            .itineraryId(1L)
            .itineraryName("즐거운 제주여정2")
            .departureTime("2023-10-26 11:00")
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
        given(itineraryService.updateTransportation(any(TransportationUpdateRequestDTO.class),
            any(Long.TYPE))).willReturn(transportationResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/itineraries/transportations")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자")
                        .attributes(key("constraints")
                            .value(updateTransportationConstraints.descriptionsForProperty(
                                "itineraryId"))),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).optional()
                        .description("여정 이름"),
                    fieldWithPath("transportation").type(JsonFieldType.STRING).optional()
                        .description("이동 수단"),
                    fieldWithPath("departurePlace").type(JsonFieldType.STRING).optional()
                        .description("출발지"),
                    fieldWithPath("departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("출발지 도로명"),
                    fieldWithPath("destination").type(JsonFieldType.STRING).optional()
                        .description("도착지"),
                    fieldWithPath("destinationRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("도착지 도로명"),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).optional()
                        .description("출발 일시"),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).optional()
                        .description("도착 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여행 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.transportation").type(JsonFieldType.STRING)
                        .description("이동 수단"),
                    fieldWithPath("data.departurePlace").type(JsonFieldType.STRING)
                        .description("출발지"),
                    fieldWithPath("data.departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .description("출발지 도로명"),
                    fieldWithPath("data.destination").type(JsonFieldType.STRING).description("도착지"),
                    fieldWithPath("data.destinationRoadAddressName").type(JsonFieldType.STRING)
                        .description("도착지 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING)
                        .description("출발 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("도착 일시")
                )
            ));
    }

    @Test
    @DisplayName("체류 여정 정보를 수정할 수 있다.")
    void updateVisit() throws Exception {
        // given
        VisitUpdateRequestDTO request = VisitUpdateRequestDTO.builder()
            .itineraryId(1L)
            .itineraryName("즐거운 제주여정3")
            .build();
        VisitResponseDTO visitResponseDTO = VisitResponseDTO.builder()
            .itineraryId(1L)
            .itineraryName("즐거운 제주여정3")
            .placeName("카멜리아힐")
            .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
            .arrivalTime("2023-10-26 14:00")
            .departureTime("2023-10-26 16:00")
            .build();
        given(itineraryService.updateVisit(any(VisitUpdateRequestDTO.class), any(Long.TYPE)))
            .willReturn(visitResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/itineraries/visits")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("itineraryId").type(JsonFieldType.NUMBER).description("여정 식별자")
                        .attributes(key("constraints")
                            .value(updateVisitConstraints.descriptionsForProperty("itineraryId"))),
                    fieldWithPath("itineraryName").type(JsonFieldType.STRING).optional()
                        .description("여정 이름"),
                    fieldWithPath("placeName").type(JsonFieldType.STRING).optional()
                        .description("장소명"),
                    fieldWithPath("placeRoadAddressName").type(JsonFieldType.STRING).optional()
                        .description("장소 도로명"),
                    fieldWithPath("departureTime").type(JsonFieldType.STRING).optional()
                        .description("도착 일시"),
                    fieldWithPath("arrivalTime").type(JsonFieldType.STRING).optional()
                        .description("출발 일시")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("장소명"),
                    fieldWithPath("data.placeRoadAddressName").type(JsonFieldType.STRING)
                        .description("장소 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING)
                        .description("도착 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING)
                        .description("출발 일시")
                )
            ));
    }

    @Test
    @DisplayName("deleteItinerary()는 여정 정보를 삭제할 수 있다.")
    void deleteItinerary() throws Exception {
        // given
        AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
            .itineraryId(1L)
            .itineraryName("제주여정1")
            .accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .checkIn("2023-10-25 15:00")
            .checkOut("2023-10-26 11:00")
            .build();
        given(itineraryService.deleteItinerary(any(Long.TYPE), any(Long.TYPE))).willReturn(
            accommodationResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/itineraries/{itineraryId}", 1L)
                    .with(user(customUserDetails))
                    .with(csrf()))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                pathParameters(parameterWithName("itineraryId").description("여정 식별자")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.itineraryId").type(JsonFieldType.NUMBER)
                        .description("여정 식별자"),
                    fieldWithPath("data.itineraryName").type(JsonFieldType.STRING)
                        .description("여정 이름"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING).optional()
                        .description("숙소명"),
                    fieldWithPath("data.accommodationRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("숙소 도로명"),
                    fieldWithPath("data.checkIn").type(JsonFieldType.STRING).optional()
                        .description("체크인 일시"),
                    fieldWithPath("data.checkOut").type(JsonFieldType.STRING).optional()
                        .description("체크아웃 일시"),
                    fieldWithPath("data.transportation").type(JsonFieldType.STRING).optional()
                        .description("이동 수단"),
                    fieldWithPath("data.departurePlace").type(JsonFieldType.STRING).optional()
                        .description("출발지"),
                    fieldWithPath("data.departurePlaceRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("출발지 도로명"),
                    fieldWithPath("data.destination").type(JsonFieldType.STRING).optional()
                        .description("도착지"),
                    fieldWithPath("data.destinationRoadAddressName").type(JsonFieldType.STRING)
                        .optional().description("도착지 도로명"),
                    fieldWithPath("data.placeName").type(JsonFieldType.STRING).optional()
                        .description("장소명"),
                    fieldWithPath("data.placeRoadAddressName").type(JsonFieldType.STRING).optional()
                        .description("장소 도로명"),
                    fieldWithPath("data.departureTime").type(JsonFieldType.STRING).optional()
                        .description("도착 일시"),
                    fieldWithPath("data.arrivalTime").type(JsonFieldType.STRING).optional()
                        .description("출발 일시")
                )
            ));
    }
}
