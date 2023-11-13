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
public class Accommodation extends Itinerary {

    @Column(length = 100)
    private String accommodationName;

    private String accommodationRoadAddressName;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    @Builder
    public Accommodation(Long id, Trip trip, String name, String accommodationName,
        String accommodationRoadAddressName,
        LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = id;
        this.trip = trip;
        this.name = name;
        this.accommodationName = accommodationName;
        this.accommodationRoadAddressName = accommodationRoadAddressName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public void updateAccommodationInfo(String name, String accommodationName,
        String accommodationRoadAddressName, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.name = name;
        this.accommodationName = accommodationName;
        this.accommodationRoadAddressName = accommodationRoadAddressName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
