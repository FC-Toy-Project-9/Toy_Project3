package com.fc.toy_project3.domain.itinerary.dto.response.get;

import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAccommodationResponseDTO {

    protected long itineraryId;
    protected String itineraryName;
    private String accommodationName;
    private String accommodationRoadAddressName;
    private String checkIn;
    private String checkOut;
    private String createdAt;
    private String updatedAt;

    @Builder
    public GetAccommodationResponseDTO(long itineraryId, String itineraryName,
        String accommodationName, String accommodationRoadAddressName, String checkIn,
        String checkOut, String createdAt, String updatedAt) {
        this.itineraryId = itineraryId;
        this.itineraryName = itineraryName;
        this.accommodationName = accommodationName;
        this.accommodationRoadAddressName = accommodationRoadAddressName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public GetAccommodationResponseDTO(Accommodation accommodation) {
        this.itineraryId = accommodation.getId();
        this.itineraryName = accommodation.getName();
        this.accommodationName = accommodation.getAccommodationName();
        this.accommodationRoadAddressName = accommodation.getAccommodationRoadAddressName();
        this.checkIn = DateTypeFormatterUtil.localDateTimeToString(accommodation.getCheckIn());
        this.checkOut = DateTypeFormatterUtil.localDateTimeToString(accommodation.getCheckOut());
        this.createdAt = DateTypeFormatterUtil.localDateTimeToString(accommodation.getCreatedAt());
        this.updatedAt = accommodation.getUpdatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(accommodation.getUpdatedAt());
    }
}
