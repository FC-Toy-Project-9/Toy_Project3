package com.fc.toy_project3.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.service.TripService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TripService tripService;

    @Mock
    private MemberService memberService;

    @Nested
    @DisplayName("postComment()는")
    class Context_postComment {

        @Test
        @DisplayName("여행 댓글 정보를 저장할 수 있다.")
        void _willSuccess() {
            // given
            CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO(1L,
                "여행 계획 정말 멋있다.");
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 계획 정말 멋있다.").build();
            given(tripService.getTrip(any(Long.TYPE))).willReturn(trip);
            given(memberService.getMember(any(Long.TYPE))).willReturn(member);
            given(commentRepository.save(any(Comment.class))).willReturn(comment);

            // when
            CommentResponseDTO result = commentService.postComment(1L, commentCreateRequestDTO);

            // then
            assertThat(result).extracting("tripId", "memberId", "content")
                .containsExactly(1L, 1L, "여행 계획 정말 멋있다.");
        }
    }

    @Nested
    @DisplayName("patchComment()는")
    class Context_patchComment {

        @Test
        @DisplayName("여행 댓글 정보를 수정할 수 있다.")
        void _willSuccess() {
            // given
            Long commentId = 1L;
            CommentUpdateRequestDTO commentUpdateRequestDTO = new CommentUpdateRequestDTO(
                "여행 정말 재밌겠다.");
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 잘 다녀와.").build();
            given(commentRepository.findById(any(Long.TYPE))).willReturn(Optional.of(comment));

            // when
            CommentResponseDTO result = commentService.patchComment(1L, commentId,
                commentUpdateRequestDTO);

            // then
            assertThat(result).extracting("tripId", "memberId", "content")
                .containsExactly(1L, 1L, "여행 정말 재밌겠다.");
        }

        @Test
        @DisplayName("특정 id를 가진 댓글을 찾을 수 없으면 수정할 수 없다.")
        void commentNotFound_willFail() {
            // given
            Optional<Comment> comment = Optional.empty();
            CommentUpdateRequestDTO commentUpdateRequestDTO = new CommentUpdateRequestDTO(
                "여행 정말 재밌겠다.");
            given(commentRepository.findById(any(Long.TYPE))).willReturn(comment);

            // when
            Throwable exception = assertThrows(CommentNotFoundException.class, () -> {
                commentService.patchComment(1L, 1L, commentUpdateRequestDTO);
            });

            // then
            assertEquals("댓글을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("특정 회원 id와 댓글의 회원 id가 같지 않으면 수정할 수 없다.")
        void commentMemberNotFound_willFail() {
            // given
            CommentUpdateRequestDTO commentUpdateRequestDTO = new CommentUpdateRequestDTO(
                "여행 정말 재밌겠다.");
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 잘 다녀와.").build();
            given(commentRepository.findById(any(Long.TYPE))).willReturn(
                Optional.ofNullable(comment));

            // when
            Throwable exception = assertThrows(CommentMemberNotFoundException.class, () -> {
                commentService.patchComment(2L, comment.getId(), commentUpdateRequestDTO);
            });

            // then
            assertEquals("댓글을 작성한 회원이 아닙니다.", exception.getMessage());
        }

        @Test
        @DisplayName("이미 삭제된 댓글이면 수정할 수 없다.")
        void commentDeleted_willFail() {
            // given
            CommentUpdateRequestDTO commentUpdateRequestDTO = new CommentUpdateRequestDTO(
                "여행 정말 재밌겠다.");
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 잘 다녀와.").build();
            comment.delete(LocalDateTime.now());
            given(commentRepository.findById(any(Long.TYPE))).willReturn(
                Optional.ofNullable(comment));

            // when
            Throwable exception = assertThrows(CommentDeletedException.class, () -> {
                commentService.patchComment(1L, comment.getId(), commentUpdateRequestDTO);
            });
            // then
            assertEquals("삭제된 댓글입니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("softDeleteComment()는")
    class Context_deleteComment {

        @Test
        @DisplayName("여행 댓글 정보를 삭제할 수 있다.")
        void _willSuccess() {
            // given
            Long commentId = 1L;
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 잘 다녀와.").build();
            given(commentRepository.findById(any(Long.TYPE))).willReturn(Optional.of(comment));

            // when
            CommentDeleteResponseDTO result = commentService.softDeleteComment(1L, commentId);

            // then
            assertThat(result.getCommentId()).isEqualTo(1);
        }

        @Test
        @DisplayName("특정 id를 가진 댓글을 찾을 수 없으면 삭제할 수 없다.")
        void commentNotFound_willFail() {
            // given
            Optional<Comment> comment = Optional.empty();
            given(commentRepository.findById(any(Long.TYPE))).willReturn(comment);

            // when
            Throwable exception = assertThrows(CommentNotFoundException.class, () -> {
                commentService.softDeleteComment(1L, 1L);
            });

            // then
            assertEquals("댓글을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("특정 회원 id와 댓글의 회원 id가 같지 않으면 수정할 수 없다.")
        void commentMemberNotFound_willFail() {
            // given
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 잘 다녀와.").build();
            given(commentRepository.findById(any(Long.TYPE))).willReturn(
                Optional.ofNullable(comment));

            // when
            Throwable exception = assertThrows(CommentMemberNotFoundException.class, () -> {
                commentService.softDeleteComment(2L, comment.getId());
            });

            // then
            assertEquals("댓글을 작성한 회원이 아닙니다.", exception.getMessage());
        }

        @Test
        @DisplayName("이미 삭제된 댓글이면 수정할 수 없다.")
        void commentDeleted_willFail() {
            // given
            Member member = Member.builder().id(1L).nickname("test").build();
            Trip trip = Trip.builder().id(1L).member(member).name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .itineraries(new ArrayList<>())
                .build();
            Comment comment = Comment.builder().id(1L).trip(trip).member(member)
                .content("여행 잘 다녀와.").build();
            comment.delete(LocalDateTime.now());
            given(commentRepository.findById(any(Long.TYPE))).willReturn(
                Optional.ofNullable(comment));

            // when
            Throwable exception = assertThrows(CommentDeletedException.class, () -> {
                commentService.softDeleteComment(1L, comment.getId());
            });

            // then
            assertEquals("삭제된 댓글입니다.", exception.getMessage());
        }
    }
}
