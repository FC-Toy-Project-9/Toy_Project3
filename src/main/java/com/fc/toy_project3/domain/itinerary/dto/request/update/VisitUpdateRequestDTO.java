package com.fc.toy_project3.domain.itinerary.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitUpdateRequestDTO {

    @NotNull(message = "여정 ID를 입력하세요.")
    private Long itineraryId;
    @Size(max = 100, message = "여정 이름은 100자 이내로 입력하세요.")
    private String itineraryName;
    @Size(max = 100, message = "장소 이름은 100자 이내로 입력하세요.")
    private String placeName;
    @Size(max = 255, message = "장소 도로명 주소는 255자 이내로 입력하세요.")
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
