package com.fc.toy_project3.domain.comment.dto.response;

import com.fc.toy_project3.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteResponseDTO {

    private Long commentId;

    @Builder
    public CommentDeleteResponseDTO(long commentId) {
        this.commentId = commentId;
    }

    public CommentDeleteResponseDTO(Comment comment) {
        this.commentId = comment.getId();
    }
}
