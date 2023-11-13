package com.fc.toy_project3.domain.trip.dto.response;

import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
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

    @Builder
    public GetTripsResponseDTO(Long tripId, String tripName, String startDate, String endDate,
        Boolean isDomestic) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
    }

    public GetTripsResponseDTO(Trip trip){
        this.tripId = trip.getId();
        this.tripName = trip.getName();
        this.startDate = DateTypeFormatterUtil.localDateToString(trip.getStartDate());
        this.endDate = DateTypeFormatterUtil.localDateToString(trip.getEndDate());
        this.isDomestic = trip.getIsDomestic();
    }
}
