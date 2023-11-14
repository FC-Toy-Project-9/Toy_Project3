package com.fc.toy_project3.domain.comment.entity;

import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 255)
    private String content;

    @Builder
    public Comment(Long id, Trip trip, Member member, String content) {
        this.id = id;
        this.trip = trip;
        this.member = member;
        this.content = content;
    }

    @Override
    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }

    public void update(CommentUpdateRequestDTO commentUpdateRequestDTO) {
        if (commentUpdateRequestDTO.getContent() != null) {
            this.content = commentUpdateRequestDTO.getContent();
        }
    }
}
