package com.fc.toy_project3.domain.itinerary.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class TransportationResponseDTO {

    private Long itineraryId;
    private String itineraryName;
    private String transportation;
    private String departurePlace;
    private String departurePlaceRoadAddressName;
    private String destination;
    private String destinationRoadAddressName;
    private String departureTime;
    private String arrivalTime;
}
