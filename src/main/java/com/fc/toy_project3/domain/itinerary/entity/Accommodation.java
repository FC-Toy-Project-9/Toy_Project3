package com.fc.toy_project3.domain.itinerary.entity;

import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
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
public class Accommodation extends Itinerary {

    @Column(length = 100)
    private String accommodationName;

    private String accommodationRoadAddressName;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    @Builder
    public Accommodation(Long id, Trip trip, String itineraryName, String accommodationName,
        String accommodationRoadAddressName,
        LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = id;
        this.trip = trip;
        this.itineraryName = itineraryName;
        this.accommodationName = accommodationName;
        this.accommodationRoadAddressName = accommodationRoadAddressName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public AccommodationResponseDTO toAccommodationResponseDTO() {
        return AccommodationResponseDTO.builder().itineraryId(super.getId())
            .itineraryName(super.getItineraryName()).accommodationName(this.accommodationName)
            .accommodationRoadAddressName(this.accommodationRoadAddressName)
            .checkIn(DateTypeFormatterUtil.localDateTimeToString(this.checkIn))
            .checkOut(DateTypeFormatterUtil.localDateTimeToString(this.checkOut)).build();
    }

    public void updateAccommodationInfo(String itineraryName, String accommodationName,
        String accommodationRoadAddressName, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.itineraryName = itineraryName;
        this.accommodationName = accommodationName;
        this.accommodationRoadAddressName = accommodationRoadAddressName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
