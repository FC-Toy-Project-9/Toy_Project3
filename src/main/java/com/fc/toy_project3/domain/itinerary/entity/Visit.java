package com.fc.toy_project3.domain.itinerary.entity;

import com.fc.toy_project3.domain.trip.entity.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Visit extends Itinerary {

    @Column(length = 100)
    private String placeName;

    private String placeRoadAddressName;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    @Builder
    public Visit(Long id, Trip trip, String itineraryName, String placeName,
        String placeRoadAddressName, LocalDateTime arrivalTime, LocalDateTime departureTime) {
        this.id = id;
        this.trip = trip;
        this.name = itineraryName;
        this.placeName = placeName;
        this.placeRoadAddressName = placeRoadAddressName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public void updateVisitInfo(String name, String placeName, String placeRoadAddressName,
        LocalDateTime visitDepartureTime, LocalDateTime visitArrivalTime) {
        this.name = name;
        this.placeName = placeName;
        this.placeRoadAddressName = placeRoadAddressName;
        this.departureTime = visitDepartureTime;
        this.arrivalTime = visitArrivalTime;
    }
}
