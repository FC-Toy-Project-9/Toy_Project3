package com.fc.toy_project3.domain.trip.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 목록 조회 페이지 응답 DTO
 */
@Getter
@NoArgsConstructor
public class GetTripsResponseDTO {

    private int totalPages;
    private Boolean isLastPage;
    private long totalTrips;
    private List<TripsResponseDTO> trips;

    @Builder
    public GetTripsResponseDTO(int totalPages, Boolean isLastPage, long totalTrips,
        List<TripsResponseDTO> trips) {
        this.totalPages = totalPages;
        this.isLastPage = isLastPage;
        this.totalTrips = totalTrips;
        this.trips = trips;
    }
}
