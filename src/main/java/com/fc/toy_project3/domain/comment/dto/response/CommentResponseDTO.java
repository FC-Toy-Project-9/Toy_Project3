package com.fc.toy_project3.domain.comment.dto.response;

import com.fc.toy_project3.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {

    private Long tripId;
    private Long memberId;
    private String content;

    @Builder
    public CommentResponseDTO(long tripId, long memberId, String content) {
        this.tripId = tripId;
        this.memberId = memberId;
        this.content = content;
    }

    public CommentResponseDTO(Comment comment) {
        this.tripId = comment.getTrip().getId();
        this.memberId = comment.getMember().getId();
        this.content = comment.getContent();
    }
}
