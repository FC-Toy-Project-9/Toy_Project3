package com.fc.toy_project3.domain.itinerary.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItineraryDeleteResponseDTO {

    private Long itineraryId;

    @Builder
    public ItineraryDeleteResponseDTO(Long itineraryId) {
        this.itineraryId = itineraryId;
    }
}