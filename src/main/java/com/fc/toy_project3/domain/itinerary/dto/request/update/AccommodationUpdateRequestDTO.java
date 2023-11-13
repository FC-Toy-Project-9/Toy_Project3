package com.fc.toy_project3.domain.itinerary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccommodationUpdateRequestDTO {

    @NotNull(message = "여정 ID를 입력하세요.")
    private Long itineraryId;
    private String itineraryName;
    private String accommodationName;
    private String accommodationRoadAddressName;
    private String checkIn;
    private String checkOut;

    @Builder
    public AccommodationUpdateRequestDTO(Long itineraryId, String itineraryName,
        String accommodationName, String accommodationRoadAddressName, String checkIn,
        String checkOut) {
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.accommodationName = accommodationName;
        this.accommodationRoadAddressName = accommodationRoadAddressName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
