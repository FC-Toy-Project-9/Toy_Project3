package com.fc.toy_project3.domain.trip.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
import com.fc.toy_project3.domain.itinerary.dto.response.get.GetAccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.get.GetTransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.get.GetVisitResponseDTO;
import com.fc.toy_project3.domain.trip.controller.TripRestController;
import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.TripPageRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.service.TripService;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 * Trip REST API 문서화 Test
 */
public class TripRestControllerDocsTest extends RestDocsSupport {

    @MockBean
    private TripService tripService;

    @Override
    public Object initController() {
        return new TripRestController(tripService);
    }

    private final ConstraintDescriptions postDescriptions = new ConstraintDescriptions(
        PostTripRequestDTO.class);

    private final ConstraintDescriptions updateDescriptions = new ConstraintDescriptions(
        UpdateTripRequestDTO.class);

    @Test
    @DisplayName("postTrip()은 여행 정보를 저장할 수 있다.")
    void postTrip() throws Exception {
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
            .likeCount(0L)
            .isDomestic(true)
            .build();
        given(tripService.postTrip(any(PostTripRequestDTO.class), any(Long.TYPE))).willReturn(trip);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(post("/api/trips", 1L)
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(postTripRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("tripName").type(JsonFieldType.STRING).description("여행 이름")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("tripName"))),
                    fieldWithPath("startDate").type(JsonFieldType.STRING).description("여행 시작일")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("startDate"))),
                    fieldWithPath("endDate").type(JsonFieldType.STRING).description("여행 종료일")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("endDate"))),
                    fieldWithPath("isDomestic").type(JsonFieldType.BOOLEAN).description("국내 여행 여부")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("isDomestic")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.tripName").type(JsonFieldType.STRING).description("여행 이름"),
                    fieldWithPath("data.startDate").type(JsonFieldType.STRING)
                        .description("여행 시작일"),
                    fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("여행 종료일"),
                    fieldWithPath("data.isDomestic").type(JsonFieldType.BOOLEAN)
                        .description("국내 여행 여부"),
                    fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                        .description("좋아요 개수")
                )
            ));
        verify(tripService, times(1)).postTrip(any(PostTripRequestDTO.class), any(Long.TYPE));
    }

    @Test
    @DisplayName("getTrips()은 여행 정보 목록을 조회할 수 있다.")
    void getTrips() throws Exception {
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
        given(tripService.getTrips(any(GetTripsRequestDTO.class), any(Pageable.class))).willReturn(
            getTripsResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(get("/api/trips")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .with(user(customUserDetails))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                        .description("총 페이지 수"),
                    fieldWithPath("data.isLastPage").type(JsonFieldType.BOOLEAN)
                        .description("마지막 페이지 여부"),
                    fieldWithPath("data.totalTrips").type(JsonFieldType.NUMBER)
                        .description("총 여행 수"),
                    fieldWithPath("data.trips").type(JsonFieldType.ARRAY).description("여행 목록"),
                    fieldWithPath("data.trips[].tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.trips[].memberId").type(JsonFieldType.NUMBER)
                        .description("회원 식별자"),
                    fieldWithPath("data.trips[].nickname").type(JsonFieldType.STRING)
                        .description("회원 닉네임"),
                    fieldWithPath("data.trips[].tripName").type(JsonFieldType.STRING)
                        .description("여행 이름"),
                    fieldWithPath("data.trips[].startDate").type(JsonFieldType.STRING)
                        .description("여행 시작일"),
                    fieldWithPath("data.trips[].endDate").type(JsonFieldType.STRING)
                        .description("여행 종료일"),
                    fieldWithPath("data.trips[].isDomestic").type(JsonFieldType.BOOLEAN)
                        .description("국내 여행 여부"),
                    fieldWithPath("data.trips[].likeCount").type(JsonFieldType.NUMBER)
                        .description("좋아요 개수"),
                    fieldWithPath("data.trips[].createdAt").type(JsonFieldType.STRING)
                        .description("생성 일시"),
                    fieldWithPath("data.trips[].updatedAt").type(JsonFieldType.STRING).optional()
                        .description("수정 일시")
                )
            ));
    }

    @Test
    @DisplayName("getLikedTrips()은 회원이 좋아요 한 여행 정보 목록을 조회할 수 있다.")
    void getLikedTrips() throws Exception {
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
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(get("/api/trips/likes", 1L)
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .with(user(customUserDetails))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                        .description("총 페이지 수"),
                    fieldWithPath("data.isLastPage").type(JsonFieldType.BOOLEAN)
                        .description("마지막 페이지 여부"),
                    fieldWithPath("data.totalTrips").type(JsonFieldType.NUMBER)
                        .description("총 여행 수"),
                    fieldWithPath("data.trips").type(JsonFieldType.ARRAY).description("여행 목록"),
                    fieldWithPath("data.trips[].tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.trips[].memberId").type(JsonFieldType.NUMBER)
                        .description("회원 식별자"),
                    fieldWithPath("data.trips[].nickname").type(JsonFieldType.STRING)
                        .description("회원 닉네임"),
                    fieldWithPath("data.trips[].tripName").type(JsonFieldType.STRING)
                        .description("여행 이름"),
                    fieldWithPath("data.trips[].startDate").type(JsonFieldType.STRING)
                        .description("여행 시작일"),
                    fieldWithPath("data.trips[].endDate").type(JsonFieldType.STRING)
                        .description("여행 종료일"),
                    fieldWithPath("data.trips[].isDomestic").type(JsonFieldType.BOOLEAN)
                        .description("국내 여행 여부"),
                    fieldWithPath("data.trips[].likeCount").type(JsonFieldType.NUMBER)
                        .description("좋아요 개수"),
                    fieldWithPath("data.trips[].createdAt").type(JsonFieldType.STRING)
                        .description("생성 일시"),
                    fieldWithPath("data.trips[].updatedAt").type(JsonFieldType.STRING).optional()
                        .description("수정 일시")
                )
            ));
    }

    @Test
    @DisplayName("getTripById()는 여행 정보를 조회할 수 있다.")
    void getTripById() throws Exception {
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
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/trips/{tripId}", 1L)
                .with(user(customUserDetails))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                pathParameters(parameterWithName("tripId").description("여행 식별자")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.tripName").type(JsonFieldType.STRING).description("여행 이름"),
                    fieldWithPath("data.startDate").type(JsonFieldType.STRING)
                        .description("여행 시작일"),
                    fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("여행 종료일"),
                    fieldWithPath("data.isDomestic").type(JsonFieldType.BOOLEAN)
                        .description("국내 여행 여부"),
                    fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                        .description("좋아요 개수"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                        .description("생성 일시"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING)
                        .optional().description("수정 일시"),
                    fieldWithPath("data.itineraries").type(JsonFieldType.ARRAY).optional()
                        .description("여정 리스트"),
                    fieldWithPath("data.itineraries[].itineraryId").type(JsonFieldType.NUMBER)
                        .optional().description("여정 식별자"),
                    fieldWithPath("data.itineraries[].itineraryName").type(JsonFieldType.STRING)
                        .optional().description("여정 이름"),
                    fieldWithPath("data.itineraries[].accommodationName").type(JsonFieldType.STRING)
                        .optional().description("숙소명"),
                    fieldWithPath("data.itineraries[].accommodationRoadAddressName").type(
                        JsonFieldType.STRING).optional().description("숙소 도로명"),
                    fieldWithPath("data.itineraries[].checkIn").type(JsonFieldType.STRING)
                        .optional().description("체크인 일시"),
                    fieldWithPath("data.itineraries[].checkOut").type(JsonFieldType.STRING)
                        .optional().description("체크아웃 일시"),
                    fieldWithPath("data.itineraries[].transportation").type(JsonFieldType.STRING)
                        .optional().description("이동 수단"),
                    fieldWithPath("data.itineraries[].departurePlace").type(JsonFieldType.STRING)
                        .optional().description("출발지"),
                    fieldWithPath("data.itineraries[].departurePlaceRoadAddressName").type(
                        JsonFieldType.STRING).optional().description("출발지 도로명"),
                    fieldWithPath("data.itineraries[].destination").type(JsonFieldType.STRING)
                        .optional().description("도착지"),
                    fieldWithPath("data.itineraries[].destinationRoadAddressName").type(
                        JsonFieldType.STRING).optional().description("도착지 도로명"),
                    fieldWithPath("data.itineraries[].departureTime").type(JsonFieldType.STRING)
                        .optional().description("출발 일시"),
                    fieldWithPath("data.itineraries[].arrivalTime").type(JsonFieldType.STRING)
                        .optional().description("도착 일시"),
                    fieldWithPath("data.itineraries[].placeName").type(JsonFieldType.STRING)
                        .optional().description("장소명"),
                    fieldWithPath("data.itineraries[].placeRoadAddressName").type(
                        JsonFieldType.STRING).optional().description("장소 도로명"),
                    fieldWithPath("data.itineraries[].departureTime").type(JsonFieldType.STRING)
                        .optional().description("도착 일시"),
                    fieldWithPath("data.itineraries[].arrivalTime").type(JsonFieldType.STRING)
                        .optional().description("출발 일시"),
                    fieldWithPath("data.itineraries[].createdAt").type(JsonFieldType.STRING)
                        .optional().description("생성 일시"),
                    fieldWithPath("data.itineraries[].updatedAt").type(JsonFieldType.STRING)
                        .optional().description("수정 일시"))));
    }

    @Test
    @DisplayName("updateTrip()은 여행 정보를 수정할 수 있다.")
    void updateTrip() throws Exception {
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
        given(tripService.updateTrip(any(UpdateTripRequestDTO.class), any(Long.TYPE))).willReturn(trip);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        // when, then
        mockMvc.perform(patch("/api/trips")
                .with(user(customUserDetails))
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(restDoc.document(requestFields(
                    fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 식별자").attributes(
                        key("constraints").value(updateDescriptions.descriptionsForProperty("id"))),
                    fieldWithPath("tripName").type(JsonFieldType.STRING).optional()
                        .description("여행 이름").attributes(key("constraints").value(
                            updateDescriptions.descriptionsForProperty("name"))),
                    fieldWithPath("startDate").type(JsonFieldType.STRING).optional()
                        .description("여행 시작일").attributes(key("constraints").value(
                            updateDescriptions.descriptionsForProperty("startDate"))),
                    fieldWithPath("endDate").type(JsonFieldType.STRING).optional()
                        .description("여행 종료일").attributes(key("constraints").value(
                            updateDescriptions.descriptionsForProperty("endDate"))),
                    fieldWithPath("isDomestic").type(JsonFieldType.BOOLEAN).optional()
                        .description("국내 여행 여부").attributes(key("constraints").value(
                            updateDescriptions.descriptionsForProperty("isDomestic")))),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.tripName").type(JsonFieldType.STRING).description("여행 이름"),
                    fieldWithPath("data.startDate").type(JsonFieldType.STRING)
                        .description("여행 시작일"),
                    fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("여행 종료일"),
                    fieldWithPath("data.isDomestic").type(JsonFieldType.BOOLEAN)
                        .description("국내 여행 여부"),
                    fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                        .description("좋아요 개수")
                )
            ));
        verify(tripService, times(1)).updateTrip(any(UpdateTripRequestDTO.class), any(Long.TYPE));
    }

    @Test
    @DisplayName("deleteTripById()은 특정 id를 가진 여행 정보를 삭제할 수 있다.")
    void deleteTripById() throws Exception {
        //given
        TripResponseDTO tripResponseDTO = TripResponseDTO.builder()
            .tripId(1L)
            .memberId(1L)
            .nickname("깜찍이")
            .tripName("제주도 여행")
            .startDate("2023-10-23")
            .endDate("2023-10-27")
            .isDomestic(true)
            .likeCount(7L)
            .build();
        given(tripService.deleteTripById(any(Long.TYPE), any(Long.TYPE))).willReturn(tripResponseDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "test", "test@mail.com",
            "test");

        //when, then
        mockMvc.perform(delete("/api/trips/{tripId}", 1L)
                .with(user(customUserDetails))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(restDoc.document(
                pathParameters(parameterWithName("tripId").description("여행 식별자")),
                responseFields(responseCommon()).and(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.tripId").type(JsonFieldType.NUMBER).description("여행 식별자"),
                    fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.tripName").type(JsonFieldType.STRING).description("여행 이름"),
                    fieldWithPath("data.startDate").type(JsonFieldType.STRING)
                        .description("여행 시작일"),
                    fieldWithPath("data.endDate").type(JsonFieldType.STRING).description("여행 종료일"),
                    fieldWithPath("data.isDomestic").type(JsonFieldType.BOOLEAN)
                        .description("국내 여행 여부"),
                    fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                        .description("좋아요 개수")
                )
            ));
        verify(tripService, times(1)).deleteTripById(any(Long.TYPE), any(Long.TYPE));
    }
}
