package com.fc.toy_project3.domain.trip.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetTripsRequestDTO {

    private final String tripName;
    private final String nickname;

    @Builder
    private GetTripsRequestDTO(String tripName, String nickname) {
        this.tripName = tripName;
        this.nickname = nickname;
    }
}
