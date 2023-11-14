package com.fc.toy_project3.domain.like.dto.response;

import com.fc.toy_project3.domain.like.entity.Like;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeResponseDTO {
    private Long likeId;
    private Long memberId;
    private Long tripId;

    @Builder
    public LikeResponseDTO(Long likeId, Long memberId, Long tripId){
        this.likeId = likeId;
        this.memberId = memberId;
        this.tripId = tripId;
    }

    public LikeResponseDTO(Like like){
        this.likeId = like.getId();
        this.memberId = like.getMember().getId();
        this.tripId = like.getTrip().getId();
    }
}