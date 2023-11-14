package com.fc.toy_project3.domain.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.toy_project3.domain.comment.entity.Comment;
import com.fc.toy_project3.domain.comment.exception.CommentNotFoundException;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("댓글이 DB에 저장이 됐습니다.")
    void saveCommet() {
        //given
        Trip trip = Trip.builder()
            .id(1L)
            .name("제주도 여행")
            .startDate(LocalDate.of(2023, 10, 25))
            .endDate(LocalDate.of(2023, 10, 26))
            .isDomestic(true)
            .itineraries(new ArrayList<>())
            .build();
        Member member = Member.builder().id(1L).email("toyproject3@gmail.com")
            .password("toypro3")
            .name("김토이").nickname("토이").build();
        tripRepository.save(trip);
        memberRepository.save(member);
        Comment comment = Comment.builder().id(1L).trip(trip).member(member).content("여행 잘 다녀와.")
            .build();

        //when
        Comment savedComment = commentRepository.save(comment);

        //then
        assertThat(comment.getId()).isEqualTo(savedComment.getId());
        assertThat(savedComment.getId()).isNotNull();
        assertThat(commentRepository.count()).isEqualTo(1);
    }


    @Test
    @DisplayName("지정된 댓글이 조회 됐습니다.")
    void findCommentById() {
        //given
        Trip trip = Trip.builder()
            .id(1L)
            .name("제주도 여행")
            .startDate(LocalDate.of(2023, 10, 25))
            .endDate(LocalDate.of(2023, 10, 26))
            .isDomestic(true)
            .itineraries(new ArrayList<>())
            .build();
        Member member = Member.builder().id(1L).email("toyproject3@gmail.com")
            .password("toypro3")
            .name("김토이").nickname("토이").build();
        tripRepository.save(trip);
        memberRepository.save(member);
        Comment comment1 = Comment.builder().id(1L).trip(trip).member(member).content("여행 잘 다녀와.")
            .build();
        Comment comment2 = Comment.builder().id(2L).trip(trip).member(member)
            .content("여행 계획 정말 멋있다.").build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //when
        Comment findComment1 = commentRepository.findById(comment1.getId()).orElseThrow(
            CommentNotFoundException::new);
        Comment findComment2 = commentRepository.findById(comment2.getId()).orElseThrow(
            CommentNotFoundException::new);

        //then
        assertThat(commentRepository.count()).isEqualTo(2);
        assertThat(findComment1.getId()).isEqualTo(1L);
        assertThat(findComment2.getId()).isEqualTo(2L);
    }

}
