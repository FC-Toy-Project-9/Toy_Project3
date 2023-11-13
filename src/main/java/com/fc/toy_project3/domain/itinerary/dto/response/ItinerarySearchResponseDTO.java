package com.fc.toy_project3.domain.itinerary.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ItinerarySearchResponseDTO {

    private String placeName;
    private String roadAddressName;
    private String placeUrl;

    @Builder
    public ItinerarySearchResponseDTO(String placeName, String roadAddressName, String placeUrl) {
        this.placeName = placeName;
        this.roadAddressName = roadAddressName;
        this.placeUrl = placeUrl;
    }
}
