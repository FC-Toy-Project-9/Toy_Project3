package com.fc.toy_project3.domain.trip.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 여행 정보 응답 DTO
 */
@Getter
@Builder
public class TripResponseDTO {

    private Long tripId;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;
}
