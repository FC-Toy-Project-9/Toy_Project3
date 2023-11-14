package com.fc.toy_project3.domain.like.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDTO {
    @NotNull(message = "좋아요를 등록할 여행 정보 ID를 입력하세요.")
    private Long tripId;
}