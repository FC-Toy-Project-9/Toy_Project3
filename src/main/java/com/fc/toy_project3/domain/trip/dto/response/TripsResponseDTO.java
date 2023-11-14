package com.fc.toy_project3.domain.trip.dto.response;

import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TripsResponseDTO {

    private Long tripId;
    private Long memberId;
    private String nickname;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;
    private Long likeCount;
    private String createdAt;
    private String updatedAt;

    @Builder
    public TripsResponseDTO(Long tripId, Long memberId, String nickname, String tripName, String startDate, String endDate,
        Boolean isDomestic, Long likeCount, String createdAt, String updatedAt) {
        this.tripId = tripId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public TripsResponseDTO(Trip trip) {
        this.tripId = trip.getId();
        this.memberId = trip.getMember().getId();
        this.nickname = trip.getMember().getNickname();
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
