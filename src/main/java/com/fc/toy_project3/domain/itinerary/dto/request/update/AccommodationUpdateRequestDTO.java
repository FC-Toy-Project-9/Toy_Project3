package com.fc.toy_project3.domain.itinerary.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccommodationUpdateRequestDTO {

    @NotNull(message = "여정 ID를 입력하세요.")
    private Long itineraryId;
    @Size(max = 100, message = "여정 이름은 100자 이내로 입력하세요.")
    private String itineraryName;
    @Size(max = 100, message = "숙소 이름은 100자 이내로 입력하세요.")
    private String accommodationName;
    @Size(max = 255, message = "숙소 도로명 주소는 255자 이내로 입력하세요.")
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
