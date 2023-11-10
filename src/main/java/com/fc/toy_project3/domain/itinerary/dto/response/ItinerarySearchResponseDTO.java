package com.fc.toy_project3.domain.itinerary.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ItinerarySearchResponseDTO {

    private String placeName;
    private String roadAddressName;
    private String placeUrl;
}
