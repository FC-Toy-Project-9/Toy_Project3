package com.fc.toy_project3.domain.trip.dto.response;

import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTripsResponseDTO {

    private Long tripId;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;
    private Long likeCount;
    private String createdAt;
    private String updatedAt;

    @Builder
    public GetTripsResponseDTO(Long tripId, String tripName, String startDate, String endDate,
        Boolean isDomestic, Long likeCount, String createdAt, String updatedAt) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public GetTripsResponseDTO(Trip trip) {
        this.tripId = trip.getId();
        this.tripName = trip.getName();
        this.startDate = DateTypeFormatterUtil.localDateToString(trip.getStartDate());
        this.endDate = DateTypeFormatterUtil.localDateToString(trip.getEndDate());
        this.isDomestic = trip.getIsDomestic();
        this.likeCount = trip.getLikeCount();
        this.createdAt = trip.getCreatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(trip.getCreatedAt());
        this.updatedAt = trip.getUpdatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(trip.getUpdatedAt());
    }
}
