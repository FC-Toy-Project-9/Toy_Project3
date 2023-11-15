package com.fc.toy_project3.domain.comment.dto.response;

import com.fc.toy_project3.domain.comment.entity.Comment;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {

    private Long tripId;
    private Long memberId;
    private String nickname;
    private String content;
    private String createdAt;
    private String updatedAt;

    @Builder
    public CommentResponseDTO(long tripId, long memberId, String nickname, String content,
        String createdAt, String updatedAt) {
        this.tripId = tripId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CommentResponseDTO(Comment comment) {
        this.tripId = comment.getTrip().getId();
        this.memberId = comment.getMember().getId();
        this.nickname = comment.getMember().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(comment.getCreatedAt());
        this.updatedAt = comment.getUpdatedAt() == null ? null
            : DateTypeFormatterUtil.localDateTimeToString(comment.getUpdatedAt());
    }
}
