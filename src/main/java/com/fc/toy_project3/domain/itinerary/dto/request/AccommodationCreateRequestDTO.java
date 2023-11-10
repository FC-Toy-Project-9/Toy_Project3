package com.fc.toy_project3.domain.itinerary.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationCreateRequestDTO {

    @NotNull(message = "여행 ID를 입력하세요.")
    private Long tripId;
    @NotBlank(message = "여정 이름을 입력하세요.")
    private String itineraryName;
    @NotBlank(message = "숙소 이름을 입력하세요.")
    private String accommodationName;
    @NotBlank(message = "숙소 도로명 주소를 입력하세요.")
    private String accommodationRoadAddressName;
    @NotBlank(message = "숙소 체크인 일시를 입력하세요.(yyyy-MM-dd HH:ss)")
    private String checkIn;
    @NotBlank(message = "숙소 체크아웃 일시를 입력하세요.(yyyy-MM-dd HH:ss)")
    private String checkOut;
}
