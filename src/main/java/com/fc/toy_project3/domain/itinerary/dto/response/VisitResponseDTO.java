package com.fc.toy_project3.domain.itinerary.dto.response;

import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class VisitResponseDTO {

    protected long itineraryId;
    protected String itineraryName;
    private String placeName;
    private String placeRoadAddressName;
    private String arrivalTime;
    private String departureTime;

    @Builder
    public VisitResponseDTO(long itineraryId, String itineraryName, String placeName,
        String placeRoadAddressName, String arrivalTime, String departureTime) {
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.placeName = placeName;
        this.placeRoadAddressName = placeRoadAddressName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public VisitResponseDTO(Visit visit){
        this.itineraryId = visit.getId();
        this.itineraryName = visit.getName();
        this.placeName = visit.getPlaceName();
        this.placeRoadAddressName = visit.getPlaceRoadAddressName();
        this.arrivalTime = DateTypeFormatterUtil.localDateTimeToString(visit.getArrivalTime());
        this.departureTime = DateTypeFormatterUtil.localDateTimeToString(visit.getDepartureTime());
    }
}
