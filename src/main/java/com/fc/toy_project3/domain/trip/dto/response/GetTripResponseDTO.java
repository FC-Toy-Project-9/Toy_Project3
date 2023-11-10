package com.fc.toy_project3.domain.trip.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTripResponseDTO {

    private Long tripId;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;
    private List<Object> itineraries;
}
