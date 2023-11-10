package com.fc.toy_project3.domain.comment.service;

import com.fc.toy_project3.domain.comment.dto.request.CommentRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.entity.Comment;
import com.fc.toy_project3.domain.comment.exception.CommentNotFoundException;
import com.fc.toy_project3.domain.comment.repository.CommentRepository;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.service.TripService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final TripService tripService;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    /**
     * 여행 댓글 등록
     *
     * @param commentRequestDTO 댓글 정보 등록 요청 DTO
     * @return 댓글 정보 응답 DTO
     */

    public CommentResponseDTO postComment(CommentRequestDTO commentRequestDTO) {
        Trip trip = tripService.getTrip(commentRequestDTO.getTripId());
        Member member = memberRepository.findById(commentRequestDTO.getMemberId()).get();
        Comment comment = Comment.builder().trip(trip).member(member)
            .content(commentRequestDTO.getContent()).build();

        return commentRepository.save(comment).toCommentResponseDTO();
    }

    /**
     * 여행 댓글 삭제
     *
     * @param replyId 댓글 식별자
     * @return
     */
    public CommentDeleteResponseDTO patchComment(Long replyId) {
        Comment comment = commentRepository.findById(replyId)
            .orElseThrow(CommentNotFoundException::new);
        comment.delete(LocalDateTime.now());
        return CommentDeleteResponseDTO.builder().replyId(comment.getId()).build();
    }


}
