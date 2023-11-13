package com.fc.toy_project3.domain.itinerary.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransportationCreateRequestDTO {

    @NotNull(message = "여행 ID를 입력하세요.")
    private Long tripId;
    @NotBlank(message = "여정 이름을 입력하세요.")
    private String itineraryName;
    @NotBlank(message = "이동 수단을 입력하세요.")
    private String transportation;
    @NotBlank(message = "출발지를 입력하세요.")
    private String departurePlace;
    @NotBlank(message = "출발지 도로명 주소를 입력하세요.")
    private String departurePlaceRoadAddressName;
    @NotBlank(message = "도착지를 입력하세요.")
    private String destination;
    @NotBlank(message = "도착지 도로명 주소를 입력하세요.")
    private String destinationRoadAddressName;
    @NotBlank(message = "출발 일시를 입력하세요.(yyyy-MM-dd HH:ss)")
    private String departureTime;
    @NotBlank(message = "도착 일시를 입력하세요.(yyyy-MM-dd HH:ss)")
    private String arrivalTime;

    @Builder
    public TransportationCreateRequestDTO(Long tripId, String itineraryName, String transportation,
        String departurePlace, String departurePlaceRoadAddressName, String destination,
        String destinationRoadAddressName, String departureTime, String arrivalTime) {
        this.tripId = tripId;
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
