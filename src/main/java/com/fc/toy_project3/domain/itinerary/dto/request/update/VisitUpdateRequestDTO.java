package com.fc.toy_project3.domain.itinerary.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitUpdateRequestDTO {

    @NotNull(message = "여정 ID를 입력하세요.")
    private Long itineraryId;
    private String itineraryName;
    private String placeName;
    private String placeRoadAddressName;
    private String arrivalTime;
    private String departureTime;

    @Builder
    public VisitUpdateRequestDTO(Long itineraryId, String itineraryName, String placeName,
        String placeRoadAddressName, String arrivalTime, String departureTime) {
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.placeName = placeName;
        this.placeRoadAddressName = placeRoadAddressName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }
}
