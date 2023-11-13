package com.fc.toy_project3.domain.itinerary.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitCreateRequestDTO {

    @NotNull(message = "여행 ID를 입력하세요.")
    private Long tripId;
    @NotBlank(message = "여정 이름을 입력하세요.")
    private String itineraryName;
    @NotBlank(message = "장소 이름을 입력하세요.")
    private String placeName;
    @NotBlank(message = "장소 도로명 주소를 입력하세요.")
    private String placeRoadAddressName;
    @NotBlank(message = "도착 일시를 입력하세요.(yyyy-MM-dd HH:ss)")
    private String arrivalTime;
    @NotBlank(message = "출발 일시를 입력하세요.(yyyy-MM-dd HH:ss)")
    private String departureTime;

    @Builder
    public VisitCreateRequestDTO(Long tripId, String itineraryName, String placeName,
        String placeRoadAddressName, String arrivalTime, String departureTime) {
        this.tripId = tripId;
        this.itineraryName = itineraryName;
        this.placeName = placeName;
        this.placeRoadAddressName = placeRoadAddressName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }
}
