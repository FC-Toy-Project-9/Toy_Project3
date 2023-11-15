package com.fc.toy_project3.domain.trip.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 100, message = "여행 이름은 100자 이내로 입력하세요.")
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