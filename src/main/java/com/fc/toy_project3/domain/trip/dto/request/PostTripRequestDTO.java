package com.fc.toy_project3.domain.trip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostTripRequestDTO {

    @NotBlank(message = "여행 이름을 입력하세요.")
    @Size(max = 100, message = "여행 이름은 100자 이내로 입력하세요.")
    private String tripName;
    @NotBlank(message = "여행 시작일을 입력하세요.(yyyy-MM-dd)")
    private String startDate;
    @NotBlank(message = "여행 종료일을 입력하세요.(yyyy-MM-dd)")
    private String endDate;
    @NotNull(message = "국내여행 여부를 입력하세요.")
    private Boolean isDomestic;

    @Builder
    public PostTripRequestDTO(String tripName, String startDate, String endDate,
        Boolean isDomestic) {
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDomestic = isDomestic;
    }
}
