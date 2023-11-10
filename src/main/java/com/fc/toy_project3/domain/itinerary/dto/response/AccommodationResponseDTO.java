package com.fc.toy_project3.domain.itinerary.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccommodationResponseDTO {

    private Long itineraryId;
    private String itineraryName;
    private String accommodationName;
    private String accommodationRoadAddressName;
    private String checkIn;
    private String checkOut;
}
