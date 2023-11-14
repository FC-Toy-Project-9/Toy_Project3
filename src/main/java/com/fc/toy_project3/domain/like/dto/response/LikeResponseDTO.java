package com.fc.toy_project3.domain.like.dto.response;

import com.fc.toy_project3.domain.like.entity.Like;
import lombok.Builder;

public class LikeResponseDTO {
    private Long likeId;
    private Long memberId;
    private Long tripId;

    @Builder
    public LikeResponseDTO(Like like){
        this.likeId = like.getId();
        this.memberId = like.getMember().getId();
        this.tripId = like.getTrip().getId();
    }
}