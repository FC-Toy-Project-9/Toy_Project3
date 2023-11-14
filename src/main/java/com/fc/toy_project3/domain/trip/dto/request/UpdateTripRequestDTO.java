package com.fc.toy_project3.domain.trip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
public class UpdateTripRequestDTO {

    @NotNull(message = "수정할 여행 정보 ID를 입력하세요.")
    private Long tripId;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;

    @Builder
    public UpdateTripRequestDTO(Long tripId, String tripName, String startDate, String endDate,
        Boolean isDomestic) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
    }
}