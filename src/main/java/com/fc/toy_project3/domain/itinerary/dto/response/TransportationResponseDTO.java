package com.fc.toy_project3.domain.itinerary.dto.response;

import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class TransportationResponseDTO {

    protected long itineraryId;
    protected String itineraryName;
    private String transportation;
    private String departurePlace;
    private String departurePlaceRoadAddressName;
    private String destination;
    private String destinationRoadAddressName;
    private String departureTime;
    private String arrivalTime;

    @Builder
    public TransportationResponseDTO(long itineraryId, String itineraryName, String transportation,
        String departurePlace, String departurePlaceRoadAddressName, String destination,
        String destinationRoadAddressName, String departureTime, String arrivalTime) {
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.transportation = transportation;
        this.departurePlace = departurePlace;
        this.departurePlaceRoadAddressName = departurePlaceRoadAddressName;
        this.destination = destination;
        this.destinationRoadAddressName = destinationRoadAddressName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public TransportationResponseDTO(Transportation transportation) {
        this.itineraryId = transportation.getId();
        this.itineraryName = transportation.getName();
        this.transportation = transportation.getTransportation();
        this.departurePlace = transportation.getDeparturePlace();
        this.departurePlaceRoadAddressName = transportation.getDeparturePlaceRoadAddressName();
        this.destination = transportation.getDestination();
        this.destinationRoadAddressName = transportation.getDestinationRoadAddressName();
        this.departureTime = DateTypeFormatterUtil.localDateTimeToString(
            transportation.getDepartureTime());
        this.arrivalTime = DateTypeFormatterUtil.localDateTimeToString(
            transportation.getArrivalTime());
    }
}
