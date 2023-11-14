package com.fc.toy_project3.domain.itinerary.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransportationUpdateRequestDTO {

    @NotNull(message = "여정 ID를 입력하세요.")
    private Long itineraryId;
    private String itineraryName;
    private String transportation;
    private String departurePlace;
    private String departurePlaceRoadAddressName;
    private String destination;
    private String destinationRoadAddressName;
    private String departureTime;
    private String arrivalTime;

    @Builder
    public TransportationUpdateRequestDTO(Long itineraryId, String itineraryName,
        String transportation, String departurePlace, String departurePlaceRoadAddressName,
        String destination, String destinationRoadAddressName, String departureTime,
        String arrivalTime) {
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
}
