package com.fc.toy_project3.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDTO {

    @NotEmpty(message = "댓글을 입력하세요.")
    private String content;
}
