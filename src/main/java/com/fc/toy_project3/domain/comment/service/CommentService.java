package com.fc.toy_project3.domain.comment.service;

import com.fc.toy_project3.domain.comment.dto.request.CommentCreateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.request.CommentUpdateRequestDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentDeleteResponseDTO;
import com.fc.toy_project3.domain.comment.dto.response.CommentResponseDTO;
import com.fc.toy_project3.domain.comment.entity.Comment;
import com.fc.toy_project3.domain.comment.exception.CommentDeletedException;
import com.fc.toy_project3.domain.comment.exception.CommentMemberNotFoundException;
import com.fc.toy_project3.domain.comment.exception.CommentNotFoundException;
import com.fc.toy_project3.domain.comment.repository.CommentRepository;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.service.TripService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final TripService tripService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    /**
     * 여행 댓글 등록
     *
     * @param commentCreateRequestDTO 댓글 정보 등록 요청 DTO
     * @return 댓글 정보 응답 DTO
     */
    public CommentResponseDTO postComment(Long memberId,
        CommentCreateRequestDTO commentCreateRequestDTO) {
        Trip trip = tripService.getTrip(commentCreateRequestDTO.getTripId());
        Member member = memberService.getMember(memberId); // memberService 함수로 변경 예정
        Comment comment = Comment.builder().trip(trip).member(member)
            .content(commentCreateRequestDTO.getContent()).build();
        commentRepository.save(comment);
        return new CommentResponseDTO(comment);
    }

    /**
     * 여행 댓글 수정
     *
     * @param commentId
     * @param commentUpdateRequestDTO
     * @return 댓글 정보 응답 DTO
     */
    public CommentResponseDTO patchComment(Long memberId, Long commentId,
        CommentUpdateRequestDTO commentUpdateRequestDTO) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);
        if (comment.getMember().getId() != memberId) {
            throw new CommentMemberNotFoundException();
        }
        if (comment.isDeleted()) {
            throw new CommentDeletedException();
        }
        comment.update(commentUpdateRequestDTO);
        return new CommentResponseDTO(comment);
    }

    /**
     * 여행 댓글 삭제
     *
     * @param commentId 댓글 식별자
     * @return 댓글 삭제 정보 응답 DTO
     */
    public CommentDeleteResponseDTO softDeleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);
        if (comment.getMember().getId() != memberId) {
            throw new CommentMemberNotFoundException();
        }
        if (comment.isDeleted()) {
            throw new CommentDeletedException();
        }
        comment.delete(LocalDateTime.now());
        return new CommentDeleteResponseDTO(comment);
    }
}
