package com.fc.toy_project3.domain.trip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 수정 요청 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTripRequestDTO {

    @NotNull(message = "수정할 여행 정보 ID를 입력하세요.")
    private Long tripId;
    @NotBlank(message = "여행 이름을 입력하세요.")
    private String tripName;
    @NotBlank(message = "여행 시작일을 입력하세요.(yyyy-MM-dd)")
    private String startDate;
    @NotBlank(message = "여행 종료일을 입력하세요.(yyyy-MM-dd)")
    private String endDate;
    @NotNull(message = "국내여행 여부를 입력하세요.")
    private Boolean isDomestic;
}