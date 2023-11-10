package com.fc.toy_project3.domain.itinerary.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fc.toy_project3.domain.itinerary.dto.request.AccommodationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.AccommodationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.TransportationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.TransportationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.VisitCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.VisitUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItineraryDeleteResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.itinerary.exception.ItineraryNotFoundException;
import com.fc.toy_project3.domain.itinerary.repository.AccommodationRepository;
import com.fc.toy_project3.domain.itinerary.repository.ItineraryRepository;
import com.fc.toy_project3.domain.itinerary.repository.TransportationRepository;
import com.fc.toy_project3.domain.itinerary.repository.VisitRepository;
import com.fc.toy_project3.domain.itinerary.service.ItineraryService;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.exception.TripNotFoundException;
import com.fc.toy_project3.domain.trip.service.TripService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {

    @InjectMocks
    private ItineraryService itineraryService;

    @Mock
    private ItineraryRepository itineraryRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private TripService tripService;

    @Nested
    @DisplayName("createAccommodation()은")
    class Context_createAccommodation {

        @Test
        @DisplayName("숙박 여정 정보를 저장할 수 있다.")
        void _willSuccess() {
            //given
            AccommodationCreateRequestDTO accommodationCreateRequestDTO = AccommodationCreateRequestDTO.builder()
                .tripId(1L).itineraryName("제주여정1").accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75").checkIn("2023-10-25 15:00")
                .checkOut("2023-10-26 11:00").build();
            given(tripService.getTrip(any(Long.TYPE))).willReturn(
                Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                    .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true)
                    .itineraries(new ArrayList<>()).build());
            given(accommodationRepository.save(any(Accommodation.class))).willReturn(
                Accommodation.builder().id(1L).itineraryName("제주여정1").accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn(LocalDateTime.of(2023, 10, 25, 15, 0))
                    .checkOut(LocalDateTime.of(2023, 10, 26, 11, 0)).build());

            // when
            AccommodationResponseDTO result = itineraryService.createAccommodation(
                accommodationCreateRequestDTO);

            // then
            assertThat(result).extracting("itineraryName", "accommodationName",
                    "accommodationRoadAddressName", "checkIn", "checkOut")
                .containsExactly("제주여정1", "제주신라호텔", "제주 서귀포시 중문관광로72번길 75", "2023-10-25 15:00",
                    "2023-10-26 11:00");
        }
    }

    @Nested
    @DisplayName("createTransportation()은")
    class Context_createTransportation {

        @Test
        @DisplayName("교통 여정 정보를 저장할 수 있다.")
        void _willSuccess() {
            //given
            TransportationCreateRequestDTO transportationCreateRequestDTO = TransportationCreateRequestDTO.builder()
                .tripId(1L).itineraryName("제주여정2").transportation("카카오택시").departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75").destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 12:00").arrivalTime("2023-10-26 13:00").build();
            given(tripService.getTrip(any(Long.TYPE))).willReturn(
                Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                    .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true)
                    .itineraries(new ArrayList<>()).build());
            given(transportationRepository.save(any(Transportation.class))).willReturn(
                Transportation.builder().id(2L).itineraryName("제주여정2").transportation("카카오택시")
                    .departurePlace("제주신라호텔").departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄").destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime(LocalDateTime.of(2023, 10, 26, 12, 0))
                    .arrivalTime(LocalDateTime.of(2023, 10, 26, 13, 0)).build());

            // when
            TransportationResponseDTO result = itineraryService.createTransportation(
                transportationCreateRequestDTO);

            // then
            assertThat(result).extracting("itineraryName", "transportation", "departurePlace",
                    "departurePlaceRoadAddressName", "destination", "destinationRoadAddressName",
                    "departureTime", "arrivalTime")
                .containsExactly("제주여정2", "카카오택시", "제주신라호텔", "제주 서귀포시 중문관광로72번길 75", "오설록 티 뮤지엄",
                    "제주 서귀포시 안덕면 신화역사로 15 오설록", "2023-10-26 12:00", "2023-10-26 13:00");
            verify(transportationRepository, times(1)).save(any(Transportation.class));
        }
    }

    @Nested
    @DisplayName("createVisit()은")
    class Context_createVisit {

        @Test
        @DisplayName("체류 여정 정보를 저장할 수 있다.")
        void _willSuccess() {
            // given
            VisitCreateRequestDTO visitCreateRequestDTO = VisitCreateRequestDTO.builder().tripId(1L)
                .itineraryName("제주여정3").placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166").arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00").build();
            given(tripService.getTrip(any(Long.TYPE))).willReturn(
                Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                    .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true)
                    .itineraries(new ArrayList<>()).build());
            given(visitRepository.save(any(Visit.class))).willReturn(
                Visit.builder().id(3L).itineraryName("제주여정3").placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime(LocalDateTime.of(2023, 10, 26, 14, 0))
                    .departureTime(LocalDateTime.of(2023, 10, 26, 16, 0)).build());

            // when
            VisitResponseDTO result = itineraryService.createVisit(visitCreateRequestDTO);

            // then
            assertThat(result).extracting("itineraryName", "placeName", "placeRoadAddressName",
                    "arrivalTime", "departureTime")
                .containsExactly("제주여정3", "카멜리아힐", "제주 서귀포시 안덕면 병악로 166", "2023-10-26 14:00",
                    "2023-10-26 16:00");
            verify(visitRepository, times(1)).save(any(Visit.class));
        }
    }

    @Nested
    @DisplayName("getItineraryByTripId()는")
    class Context_getItineraryByTripId {

        @Test
        @DisplayName("tripId를 통해 Itinerary를 조회할 수 있다.")
        void _willSuccess() throws TripNotFoundException {
            // given
            Itinerary accommodation = Accommodation.builder().id(1L).itineraryName("제주여정1")
                .accommodationName("제주신라호텔").accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn(LocalDateTime.of(2023, 10, 25, 15, 0))
                .checkOut(LocalDateTime.of(2023, 10, 26, 11, 0)).build();

            Itinerary transportation = Transportation.builder().id(2L).itineraryName("제주여정2")
                .transportation("카카오택시").departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75").destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime(LocalDateTime.of(2023, 10, 26, 12, 0))
                .arrivalTime(LocalDateTime.of(2023, 10, 26, 13, 0)).build();

            Itinerary visit = Visit.builder().id(3L).itineraryName("제주여정2")
                .placeName("카멜리아힐").placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime(LocalDateTime.of(2023, 10, 26, 14, 0))
                .departureTime(LocalDateTime.of(2023, 10, 26, 16, 0)).build();

            List<Itinerary> itineraryList = new ArrayList<>();
            itineraryList.add(accommodation);
            itineraryList.add(transportation);
            itineraryList.add(visit);
            Trip trip = Trip.builder().itineraries(itineraryList).build();
            given(tripService.getTrip(any(Long.TYPE))).willReturn(trip);

            // when
            List<Object> itineraryResponseList = itineraryService.getItineraryByTripId(1L);

            // then
            assertThat(itineraryResponseList.get(0)).extracting("itineraryName",
                    "accommodationName", "accommodationRoadAddressName")
                .containsExactly("제주여정1", "제주신라호텔", "제주 서귀포시 중문관광로72번길 75");
        }

    }

    @Nested
    @DisplayName("updateAccommodation()은")
    class Context_updateAccommodation {

        @Test
        @DisplayName("숙박 여정 정보를 수정할 수 있다.")
        void _willSuccess() {
            // given
            AccommodationUpdateRequestDTO accommodationUpdateRequestDTO = AccommodationUpdateRequestDTO.builder()
                .itineraryId(1L).itineraryName("즐거운 제주여정1").accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75").checkIn("2023-10-25 15:00")
                .checkOut("2023-10-26 10:00").build();
            Optional<Itinerary> itinerary = Optional.of(
                Accommodation.builder().id(1L).itineraryName("제주여정1").accommodationName("제주신라호텔")
                    .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .checkIn(LocalDateTime.of(2023, 10, 25, 15, 0))
                    .checkOut(LocalDateTime.of(2023, 10, 26, 11, 0)).trip(
                        Trip.builder().id(1L).startDate(LocalDate.of(2023, 10, 25))
                            .endDate(LocalDate.of(2023, 10, 26)).build()).build());
            given(itineraryRepository.findById(any(Long.TYPE))).willReturn(itinerary);

            // when
            AccommodationResponseDTO result = itineraryService.updateAccommodation(
                accommodationUpdateRequestDTO);

            // then
            assertThat(result).extracting("itineraryName", "accommodationName",
                    "accommodationRoadAddressName", "checkIn", "checkOut")
                .containsExactly("즐거운 제주여정1", "제주신라호텔", "제주 서귀포시 중문관광로72번길 75", "2023-10-25 15:00",
                    "2023-10-26 10:00");
        }
    }

    @Nested
    @DisplayName("updateTransportation()은")
    class Context_updateTransportation {

        @Test
        @DisplayName("교통 여정 정보를 수정할 수 있다.")
        void patchTransportation_willSuccess() {
            // given
            TransportationUpdateRequestDTO transportationUpdateRequestDTO = TransportationUpdateRequestDTO.builder()
                .itineraryId(1L).itineraryName("즐거운 제주여정2").transportation("카카오택시")
                .departurePlace("제주신라호텔").departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄").destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime("2023-10-26 12:00").arrivalTime("2023-10-26 13:00").build();
            Optional<Itinerary> itinerary = Optional.of(
                Transportation.builder().id(2L).itineraryName("제주여정2").transportation("카카오택시")
                    .departurePlace("제주신라호텔").departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                    .destination("오설록 티 뮤지엄").destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                    .departureTime(LocalDateTime.of(2023, 10, 26, 12, 0))
                    .arrivalTime(LocalDateTime.of(2023, 10, 26, 13, 0)).trip(
                        Trip.builder().id(1L).startDate(LocalDate.of(2023, 10, 25))
                            .endDate(LocalDate.of(2023, 10, 26)).build()).build());
            given(itineraryRepository.findById(any(Long.TYPE))).willReturn(itinerary);

            // when
            TransportationResponseDTO result = itineraryService.updateTransportation(
                transportationUpdateRequestDTO);

            //then
            assertThat(result).extracting("itineraryName", "transportation", "departurePlace",
                    "departurePlaceRoadAddressName", "destination", "destinationRoadAddressName",
                    "departureTime", "arrivalTime")
                .containsExactly("즐거운 제주여정2", "카카오택시", "제주신라호텔", "제주 서귀포시 중문관광로72번길 75",
                    "오설록 티 뮤지엄", "제주 서귀포시 안덕면 신화역사로 15 오설록", "2023-10-26 12:00",
                    "2023-10-26 13:00");
        }
    }

    @Nested
    @DisplayName("updateVisit()은")
    class Context_updateVisit {

        @Test
        @DisplayName("체류 여정 정보를 수정할 수 있다.")
        void patchVisit_willSuccess() {
            // given
            VisitUpdateRequestDTO visitUpdateRequestDTO = VisitUpdateRequestDTO.builder()
                .itineraryId(1L).itineraryName("즐거운 제주여정3").placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166").arrivalTime("2023-10-26 14:00")
                .departureTime("2023-10-26 16:00").build();
            Optional<Itinerary> itinerary = Optional.of(
                Visit.builder().id(3L).itineraryName("제주여정3").placeName("카멜리아힐")
                    .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                    .arrivalTime(LocalDateTime.of(2023, 10, 26, 14, 0))
                    .departureTime(LocalDateTime.of(2023, 10, 26, 16, 0)).trip(
                        Trip.builder().id(1L).startDate(LocalDate.of(2023, 10, 25))
                            .endDate(LocalDate.of(2023, 10, 26)).build()).build());
            given(itineraryRepository.findById(any(Long.TYPE))).willReturn(itinerary);

            // when
            VisitResponseDTO result = itineraryService.updateVisit(visitUpdateRequestDTO);

            // then
            assertThat(result).extracting("itineraryName", "placeName", "placeRoadAddressName",
                    "arrivalTime", "departureTime")
                .containsExactly("즐거운 제주여정3", "카멜리아힐", "제주 서귀포시 안덕면 병악로 166", "2023-10-26 14:00",
                    "2023-10-26 16:00");
        }
    }

    @Nested
    @DisplayName("deleteItinerary()는")
    class Context_deleteItinerary {

        @Test
        @DisplayName("ItineraryId를 통해 Itinerary를 삭제할 수 있다.")
        void _willSuccess() throws ItineraryNotFoundException {
            // given
            Long itineraryId = 1L;
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 23))
                .endDate(LocalDate.of(2023, 10, 27)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Itinerary itinerary = Accommodation.builder().id(1L).trip(trip).build();
            when(itineraryRepository.findById(itineraryId)).thenReturn(Optional.of(itinerary));

            // when
            ItineraryDeleteResponseDTO itineraryGetResponseDTO = itineraryService.deleteItinerary(
                itineraryId);

            //then
            verify(itineraryRepository, times(1)).delete(itinerary);

        }
    }
}
