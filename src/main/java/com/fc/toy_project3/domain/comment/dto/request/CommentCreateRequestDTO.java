package com.fc.toy_project3.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDTO {

    @NotNull(message = "여행 ID를 입력하세요.")
    private Long tripId;
    @NotEmpty(message = "댓글을 입력하세요.")
    private String content;

    @Builder
    public CommentCreateRequestDTO(Long tripId, String content) {
        this.tripId = tripId;
        this.content = content;
    }

}
