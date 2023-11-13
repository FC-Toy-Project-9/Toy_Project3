package com.fc.toy_project3.domain.itinerary.entity;

import com.fc.toy_project3.domain.itinerary.dto.request.update.TransportationUpdateRequestDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Transportation extends Itinerary {

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
    public Transportation(Long id, Trip trip, String itineraryName, String transportation,
        String departurePlace, String departurePlaceRoadAddressName, String destination,
        String destinationRoadAddressName, LocalDateTime arrivalTime, LocalDateTime departureTime) {
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

    public void updateTransportationInfo(
        TransportationUpdateRequestDTO transportationUpdateRequestDTO) {
        if (name != null) {
            this.name = transportationUpdateRequestDTO.getItineraryName();
        }
        if (transportation != null) {
            this.transportation = transportationUpdateRequestDTO.getTransportation();
        }
        if (departurePlace != null) {
            this.departurePlace = transportationUpdateRequestDTO.getDeparturePlace();
        }
        if (departurePlaceRoadAddressName != null) {
            this.departurePlaceRoadAddressName = transportationUpdateRequestDTO.getDeparturePlaceRoadAddressName();
        }
        if (destination != null) {
            this.destination = transportationUpdateRequestDTO.getDestination();
        }
        if (destinationRoadAddressName != null) {
            this.destinationRoadAddressName = transportationUpdateRequestDTO.getDestinationRoadAddressName();
        }
        if (departureTime != null) {
            this.departureTime = DateTypeFormatterUtil.dateTimeFormatter(
                transportationUpdateRequestDTO.getDepartureTime());
        }
        if (arrivalTime != null) {
            this.arrivalTime = DateTypeFormatterUtil.dateTimeFormatter(
                transportationUpdateRequestDTO.getArrivalTime());
        }
    }
}
