package com.fc.toy_project3.domain.trip.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

/**
 * 여행 조회 시 검색어 DTO
 */
@Getter
public class GetTripsRequestDTO {

    @Size(max = 100, message = "여행 이름은 100자 이내로 입력하세요.")
    private final String tripName;
    @Size(max = 30, message = "닉네임은 30자 이내로 입력하세요.")
    private final String nickname;

    @Builder
    private GetTripsRequestDTO(String tripName, String nickname) {
        this.tripName = tripName;
        this.nickname = nickname;
    }
}
