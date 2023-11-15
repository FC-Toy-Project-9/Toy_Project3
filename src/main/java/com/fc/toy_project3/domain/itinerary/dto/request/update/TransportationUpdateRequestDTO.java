package com.fc.toy_project3.domain.itinerary.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransportationUpdateRequestDTO {

    @NotNull(message = "여정 ID를 입력하세요.")
    private Long itineraryId;
    @Size(max = 100, message = "여정 이름은 100자 이내로 입력하세요.")
    private String itineraryName;
    @Size(max = 100, message = "이동 수단은 100자 이내로 입력하세요.")
    private String transportation;
    @Size(max = 100, message = "출발지는 100자 이내로 입력하세요.")
    private String departurePlace;
    @Size(max = 255, message = "출발지 도로명 주소은 100자 이내로 입력하세요.")
    private String departurePlaceRoadAddressName;
    @Size(max = 100, message = "도착지는 100자 이내로 입력하세요.")
    private String destination;
    @Size(max = 255, message = "도착지 도로명 주소는 100자 이내로 입력하세요.")
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
