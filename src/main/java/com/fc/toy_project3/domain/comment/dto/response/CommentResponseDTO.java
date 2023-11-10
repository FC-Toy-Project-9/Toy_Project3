package com.fc.toy_project3.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDTO {

    private Long tripId;
    private Long memberId;
    private String content;
}
