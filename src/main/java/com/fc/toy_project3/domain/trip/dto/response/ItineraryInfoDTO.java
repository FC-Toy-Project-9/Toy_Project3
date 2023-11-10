package com.fc.toy_project3.domain.trip.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItineraryInfoDTO {

    private Long itineraryId;
    private String itineraryName;
}
