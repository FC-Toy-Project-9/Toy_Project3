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
public class Transportation extends Itinerary{

    @Column(length = 100)
    private String transportation;

    @Column(length = 100)
    private String departurePlace;

    private String departurePlaceRoadAddressName;

    @Column(length = 100)
    private String destination;

    private String destinationRoadAddressName;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    @Builder
    public Transportation(Long id, Trip trip, String itineraryName, String transportation, String departurePlace,
        String departurePlaceRoadAddressName, String destination, String destinationRoadAddressName,
        LocalDateTime arrivalTime, LocalDateTime departureTime) {
        this.id = id;
        this.trip = trip;
        this.name = itineraryName;
        this.transportation = transportation;
        this.departurePlace = departurePlace;
        this.departurePlaceRoadAddressName = departurePlaceRoadAddressName;
        this.destination = destination;
        this.destinationRoadAddressName = destinationRoadAddressName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public void updateTransportationInfo(String name, String transportation,
        String departurePlace, String departurePlaceRoadAddressName, String destination,
        String destinationRoadAddressName, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.name = name;
        this.transportation = transportation;
        this.departurePlace = departurePlace;
        this.departurePlaceRoadAddressName = departurePlaceRoadAddressName;
        this.destination = destination;
        this.destinationRoadAddressName = destinationRoadAddressName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
