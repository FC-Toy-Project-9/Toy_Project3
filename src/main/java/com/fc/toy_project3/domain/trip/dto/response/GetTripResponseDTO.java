package com.fc.toy_project3.domain.trip.dto.response;

import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTripResponseDTO {

    private Long tripId;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;
    private Long likeCount;
    private String createdAt;
    private String updatedAt;
    private List<Object> itineraries;

    @Builder
    public GetTripResponseDTO(Long tripId, String tripName, String startDate, String endDate,
        Boolean isDomestic, Long likeCount, String createdAt, String updatedAt,
        List<Object> itineraries) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.itineraries = itineraries;
    }

    public GetTripResponseDTO(Trip trip) {
        this.tripId = trip.getId();
        this.tripName = trip.getName();
        this.startDate = DateTypeFormatterUtil.localDateToString(trip.getStartDate());
        this.endDate = DateTypeFormatterUtil.localDateToString(trip.getEndDate());
        this.isDomestic = trip.getIsDomestic();
        this.likeCount = trip.getLikeCount();
        this.createdAt = trip.getCreatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(trip.getCreatedAt());
        this.updatedAt = trip.getUpdatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(trip.getUpdatedAt());
        this.itineraries = getItineraryResponseDTO(trip);
    }

    public List<Object> getItineraryResponseDTO(Trip trip) {
        List<Object> itineraryList = new ArrayList<>();
        for (Itinerary itinerary : trip.getItineraries()) {
            if (itinerary instanceof Accommodation accommodation) {
                itineraryList.add(
                    AccommodationResponseDTO.builder().itineraryId(accommodation.getId())
                        .itineraryName(accommodation.getName())
                        .accommodationName(accommodation.getAccommodationName())
                        .accommodationRoadAddressName(
                            accommodation.getAccommodationRoadAddressName())
                        .checkIn(
                            DateTypeFormatterUtil.localDateTimeToString(accommodation.getCheckIn()))
                        .checkOut(DateTypeFormatterUtil.localDateTimeToString(
                            accommodation.getCheckOut()))
                        .build());
            } else if (itinerary instanceof Transportation transportation) {
                itineraryList.add(
                    TransportationResponseDTO.builder().itineraryId(transportation.getId())
                        .itineraryName(transportation.getName())
                        .transportation(transportation.getTransportation())
                        .departurePlace(transportation.getDeparturePlace())
                        .departurePlaceRoadAddressName(
                            transportation.getDeparturePlaceRoadAddressName())
                        .destination(transportation.getDestination())
                        .destinationRoadAddressName(transportation.getDestinationRoadAddressName())
                        .departureTime(
                            DateTypeFormatterUtil.localDateTimeToString(
                                transportation.getDepartureTime()))
                        .arrivalTime(
                            DateTypeFormatterUtil.localDateTimeToString(
                                transportation.getArrivalTime()))
                        .build());
            } else if (itinerary instanceof Visit visit) {
                itineraryList.add(VisitResponseDTO.builder().itineraryId(visit.getId())
                    .itineraryName(visit.getName()).placeName(visit.getPlaceName())
                    .placeRoadAddressName(visit.getPlaceRoadAddressName()).arrivalTime(
                        DateTypeFormatterUtil.localDateTimeToString(visit.getArrivalTime()))
                    .departureTime(
                        DateTypeFormatterUtil.localDateTimeToString(visit.getDepartureTime()))
                    .build());
            }
        }
        return itineraryList;
    }
}
