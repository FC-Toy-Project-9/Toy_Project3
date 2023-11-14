package com.fc.toy_project3.domain.trip.dto.response;

import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 정보 응답 DTO
 */
@Getter
@NoArgsConstructor
public class TripResponseDTO {

    private Long tripId;
    private Long memberId;
    private String nickname;
    private String tripName;
    private String startDate;
    private String endDate;
    private Boolean isDomestic;
    private Long likeCount;

    @Builder
    public TripResponseDTO(Long tripId, Long memberId, String nickname, String tripName, String startDate, String endDate,
        Long likeCount, Boolean isDomestic) {
        this.tripId = tripId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
        this.likeCount = likeCount;
    }

    public TripResponseDTO(Trip trip) {
        this.tripId = trip.getId();
        this.memberId = trip.getMember().getId();
        this.nickname = trip.getMember().getNickname();
        this.tripName = trip.getName();
        this.startDate = DateTypeFormatterUtil.localDateToString(trip.getStartDate());
        this.endDate = DateTypeFormatterUtil.localDateToString(trip.getEndDate());
        this.isDomestic = trip.getIsDomestic();
        this.likeCount = trip.getLikeCount();
    }
}
