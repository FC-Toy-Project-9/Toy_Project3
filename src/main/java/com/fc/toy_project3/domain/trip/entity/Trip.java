package com.fc.toy_project3.domain.trip.entity;

import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.global.common.BaseTimeEntity;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * Trip Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Trip extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Comment("국내 = 1, 국외 = 0")
    private Boolean isDomestic;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Itinerary> itineraries = new ArrayList<>();

    /**
     * Builder 패턴이 적용된 Trip Entity 생성자
     *
     * @param id          여행 ID
     * @param name        여행 이름
     * @param startDate   여행 시작일
     * @param endDate     여행 종료일
     * @param itineraries 해당 여행의 여정 리스트
     */
    @Builder
    public Trip(Long id, String name, LocalDate startDate, LocalDate endDate, Boolean isDomestic,
        List<Itinerary> itineraries) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
        this.itineraries = itineraries;
    }

    /**
     * Trip Entity를 TripResponseDTO로 변환
     *
     * @return TripResponseDTO
     */
    public TripResponseDTO toTripResponseDTO() {
        return TripResponseDTO.builder().tripId(this.id).tripName(this.name)
            .startDate(DateTypeFormatterUtil.localDateToString(this.startDate))
            .endDate(DateTypeFormatterUtil.localDateToString(this.endDate))
            .isDomestic(this.isDomestic).build();
    }

    /**
     * Trip Entity를 GetTripsResponseDTO로 변환
     *
     * @return GetTripsResponseDTO
     */
    public GetTripsResponseDTO toGetTripsResponseDTO() {
        return GetTripsResponseDTO.builder().tripId(this.id).tripName(this.name)
            .startDate(DateTypeFormatterUtil.localDateToString(this.startDate))
            .endDate(DateTypeFormatterUtil.localDateToString(this.endDate))
            .isDomestic(this.isDomestic).build();
    }

    /**
     * Trip Entity를 GetTripsResponseDTO로 변환
     *
     * @return GetTripsResponseDTO
     */
    public GetTripResponseDTO toGetTripResponseDTO() {
        return GetTripResponseDTO.builder().tripId(this.id).tripName(this.name)
            .startDate(DateTypeFormatterUtil.localDateToString(this.startDate))
            .endDate(DateTypeFormatterUtil.localDateToString(this.endDate))
            .isDomestic(this.isDomestic).itineraries(getItineraryResponseDTO()).build();
    }

    public List<Object> getItineraryResponseDTO() {
        List<Object> itineraryList = new ArrayList<>();
        for (Itinerary itinerary : this.itineraries) {
            if (itinerary instanceof Accommodation accommodation) {
                itineraryList.add(AccommodationResponseDTO.builder().itineraryId(accommodation.getId())
                    .itineraryName(accommodation.getItineraryName())
                    .accommodationName(accommodation.getAccommodationName())
                    .accommodationRoadAddressName(accommodation.getAccommodationRoadAddressName())
                    .checkIn(DateTypeFormatterUtil.localDateTimeToString(accommodation.getCheckIn()))
                    .checkOut(DateTypeFormatterUtil.localDateTimeToString(accommodation.getCheckOut()))
                    .build());
            } else if (itinerary instanceof Transportation transportation) {
                itineraryList.add(TransportationResponseDTO.builder().itineraryId(transportation.getId())
                    .itineraryName(transportation.getItineraryName())
                    .transportation(transportation.getTransportation())
                    .departurePlace(transportation.getDeparturePlace())
                    .departurePlaceRoadAddressName(transportation.getDeparturePlaceRoadAddressName())
                    .destination(transportation.getDestination())
                    .destinationRoadAddressName(transportation.getDestinationRoadAddressName())
                    .departureTime(
                        DateTypeFormatterUtil.localDateTimeToString(transportation.getDepartureTime()))
                    .arrivalTime(
                        DateTypeFormatterUtil.localDateTimeToString(transportation.getArrivalTime()))
                    .build());
            } else if (itinerary instanceof Visit visit) {
                itineraryList.add(VisitResponseDTO.builder().itineraryId(visit.getId())
                    .itineraryName(visit.getItineraryName()).placeName(visit.getPlaceName())
                    .placeRoadAddressName(visit.getPlaceRoadAddressName()).arrivalTime(
                        DateTypeFormatterUtil.localDateTimeToString(visit.getArrivalTime()))
                    .departureTime(
                        DateTypeFormatterUtil.localDateTimeToString(visit.getDepartureTime()))
                    .build());
            }
        }
        return itineraryList;
    }

    /**
     * 여행 정보를 수정
     *
     * @param updateTripRequestDTO 여행 정보 수정 요청 DTO
     */
    public void updateTrip(UpdateTripRequestDTO updateTripRequestDTO) {
        this.name = updateTripRequestDTO.getTripName();
        this.startDate = DateTypeFormatterUtil.dateFormatter(updateTripRequestDTO.getStartDate());
        this.endDate = DateTypeFormatterUtil.dateFormatter(updateTripRequestDTO.getEndDate());
        this.isDomestic = updateTripRequestDTO.getIsDomestic();
    }
}
