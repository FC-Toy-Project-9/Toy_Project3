package com.fc.toy_project3.domain.itinerary.dto.response.get;

import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class GetVisitResponseDTO {

    protected long itineraryId;
    protected String itineraryName;
    private String placeName;
    private String placeRoadAddressName;
    private String arrivalTime;
    private String departureTime;
    private String createdAt;
    private String updatedAt;

    @Builder
    public GetVisitResponseDTO(long itineraryId, String itineraryName, String placeName,
        String placeRoadAddressName, String arrivalTime, String departureTime, String createdAt,
        String updatedAt) {
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.placeName = placeName;
        this.placeRoadAddressName = placeRoadAddressName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public GetVisitResponseDTO(Visit visit) {
        this.itineraryId = visit.getId();
        this.itineraryName = visit.getName();
        this.placeName = visit.getPlaceName();
        this.placeRoadAddressName = visit.getPlaceRoadAddressName();
        this.arrivalTime = DateTypeFormatterUtil.localDateTimeToString(visit.getArrivalTime());
        this.departureTime = DateTypeFormatterUtil.localDateTimeToString(visit.getDepartureTime());
        this.createdAt = DateTypeFormatterUtil.localDateTimeToString(visit.getCreatedAt());
        this.updatedAt = visit.getUpdatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(visit.getUpdatedAt());
    }
}
