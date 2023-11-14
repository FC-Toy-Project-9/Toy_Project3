package com.fc.toy_project3.domain.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.toy_project3.domain.comment.entity.Comment;
import com.fc.toy_project3.domain.comment.exception.CommentNotFoundException;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestJpaConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        commentRepository.deleteAll();
        tripRepository.deleteAll();
        memberRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE trip").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE member").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE comment").executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE trip ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE comment ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }


    @Test
    @DisplayName("댓글이 DB에 저장이 됐습니다.")
    void saveComment() {
        // given
        Member member = Member.builder().id(1L).email("toyproject3@gmail.com")
            .password("toypro3")
            .name("김토이").nickname("토이").build();
        Trip trip = Trip.builder()
            .id(1L)
            .member(member)
            .name("제주도 여행")
            .startDate(LocalDate.of(2023, 10, 25))
            .endDate(LocalDate.of(2023, 10, 26))
            .isDomestic(true)
            .itineraries(new ArrayList<>())
            .build();

        tripRepository.save(trip);
        memberRepository.save(member);
        Comment comment = Comment.builder().id(1L).trip(trip).member(member).content("여행 잘 다녀와.")
            .build();

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(comment.getId()).isEqualTo(savedComment.getId());
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getTrip().getId()).isEqualTo(1L);
        assertThat(savedComment.getMember().getId()).isEqualTo(1L);
        assertThat(savedComment.getContent()).isNotNull();
        assertThat(savedComment.getCreatedAt()).isNotNull();
        assertThat(commentRepository.count()).isEqualTo(1);
    }


    @Test
    @DisplayName("지정된 댓글이 조회 됐습니다.")
    void findCommentById() {
        // given
        Member member1 = Member.builder().id(1L).nickname("닉네임1").build();
        Member member2 = Member.builder().id(2L).nickname("닉네임1").build();
        Trip trip1 = Trip.builder()
            .id(1L)
            .member(member1)
            .name("제주도 여행")
            .startDate(LocalDate.of(2023, 10, 25))
            .endDate(LocalDate.of(2023, 10, 26))
            .isDomestic(true)
            .itineraries(new ArrayList<>())
            .build();
        Trip trip2 = Trip.builder()
            .id(2L)
            .member(member2)
            .name("속초 겨울바다 여행")
            .startDate(LocalDate.of(2023, 11, 27))
            .endDate(LocalDate.of(2023, 11, 29))
            .isDomestic(true)
            .likeCount(10L)
            .itineraries(new ArrayList<>())
            .build();
        Comment comment1 = Comment.builder().id(1L).trip(trip1).member(member1).content("여행 잘 다녀와.")
            .build();
        Comment comment2 = Comment.builder().id(2L).trip(trip2).member(member2)
            .content("여행 계획 정말 멋있다.").build();

        memberRepository.saveAll(List.of(member1, member2));
        tripRepository.saveAll(List.of(trip1, trip2));
        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        Comment findComment1 = commentRepository.findById(comment1.getId())
            .orElseThrow(CommentNotFoundException::new);

        Comment findComment2 = commentRepository.findById(comment2.getId())
            .orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(findComment1.getId()).isEqualTo(1L);
        assertThat(findComment1.getTrip().getId()).isEqualTo(1L);
        assertThat(findComment1.getMember().getId()).isEqualTo(1L);
        assertThat(findComment2.getId()).isEqualTo(2L);
        assertThat(findComment2.getTrip().getId()).isEqualTo(2L);
        assertThat(findComment2.getMember().getId()).isEqualTo(2L);
        assertThat(commentRepository.count()).isEqualTo(2);
    }

}
