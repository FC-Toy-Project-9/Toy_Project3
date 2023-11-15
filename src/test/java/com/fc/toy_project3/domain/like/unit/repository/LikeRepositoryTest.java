package com.fc.toy_project3.domain.like.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fc.toy_project3.config.TestJpaConfig;
import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.dto.request.TripPageRequestDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({TestJpaConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LikeRepositoryTest {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        likeRepository.deleteAll();
        memberRepository.deleteAll();
        tripRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE likes").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE member").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE trip").executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE likes ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE trip ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Nested
    @DisplayName("save()는")
    class Context_save {

        @Test
        @DisplayName("좋아요 정보를 저장할 수 있다.")
        void _willSuccess() {
            //given
            Long memberId = 1L, tripId = 1L;
            Member member = Member.builder().id(memberId).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(tripId).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();

            if(!memberRepository.existsById(memberId)){
                memberRepository.save(member);
            }
            if(!tripRepository.existsById(tripId)){
                tripRepository.save(trip);
            }

            Like like = Like.builder().id(1L).trip(trip).member(member).build();

            //when
            Like result = likeRepository.save(like);

            //then
            assertEquals(like.getId(), result.getId());
            assertEquals(member.getId(), result.getMember().getId());
            assertEquals(trip.getId(), result.getTrip().getId());
        }
    }

    @Nested
    @DisplayName("findByMemberIdAndTripId()는")
    class Context_findByMemberIdAndTripId {

        @Test
        @DisplayName("특정 회원 id와 여행 id를 가진 좋아요 정보 Entity를 조회할 수 있다.")
        void _willSuccess() {
            //given
            Long memberId = 1L, tripId = 1L;
            Member member = Member.builder().id(memberId).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(tripId).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(1L).trip(trip).member(member).build();

            if(!memberRepository.existsById(memberId)){
                memberRepository.save(member);
            }
            if(!tripRepository.existsById(tripId)){
                tripRepository.save(trip);
            }
            likeRepository.save(like);


            // when
            Optional<Like> result = likeRepository.findByMemberIdAndTripId(memberId, tripId);

            //then
            assertNotNull(result.get().getId());
            assertEquals(memberId, result.get().getMember().getId());
            assertEquals(tripId, result.get().getTrip().getId());
        }
    }

    @Nested
    @DisplayName("findAllByMemberIdAndPageable()은")
    class Context_findAllByMemberIdAndPageable {

        @Test
        @DisplayName("특정 회원 id를 가진 좋아요 정보 리스트 Page를 조회할 수 있다.")
        void _willSuccess() {
            // given
            long memberId = 1L;
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("createdAt")
                .sort("ASC")
                .build().of();
            Member member1 = Member.builder().id(1L).nickname("닉네임1").build();
            Member member2 = Member.builder().id(2L).nickname("닉네임2").build();
            Member member3 = Member.builder().id(3L).nickname("닉네임3").build();
            memberRepository.saveAll(List.of(member1, member2, member3));
            Trip trip1 = Trip.builder()
                .id(1L)
                .member(member1)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 23))
                .endDate(LocalDate.of(2023, 10, 27))
                .isDomestic(true)
                .likeCount(0L)
                .itineraries(new ArrayList<>())
                .build();
            Trip trip2 = Trip.builder()
                .id(2L)
                .member(member2)
                .name("속초 겨울바다 여행")
                .startDate(LocalDate.of(2023, 11, 27))
                .endDate(LocalDate.of(2023, 11, 29))
                .isDomestic(true)
                .likeCount(0L)
                .itineraries(new ArrayList<>())
                .build();
            Trip trip3 = Trip.builder()
                .id(3L)
                .member(member3)
                .name("크리스마스 미국 여행")
                .startDate(LocalDate.of(2023, 12, 24))
                .endDate(LocalDate.of(2023, 12, 26))
                .isDomestic(false)
                .likeCount(0L)
                .itineraries(new ArrayList<>())
                .build();
            tripRepository.saveAll(List.of(trip1, trip2, trip3));
            Like like1 = Like.builder()
                .id(1L)
                .trip(trip1)
                .member(member1)
                .build();
            Like like2 = Like.builder()
                .id(2L)
                .trip(trip2)
                .member(member1)
                .build();
            Like like3 = Like.builder()
                .id(3L)
                .trip(trip3)
                .member(member1)
                .build();
            likeRepository.saveAll(List.of(like1, like2, like3));

            // when
            Page<Like> result = likeRepository.findAllByMemberIdAndPageable(memberId, pageable);

            //then
            assertThat(result.getTotalPages()).isEqualTo(1L);
            assertThat(result.isLast()).isTrue();
            assertThat(result.getTotalElements()).isEqualTo(3L);
            assertThat(result.get()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("findById()는")
    class Context_findById {

        @Test
        @DisplayName("특정 id를 가진 좋아요 정보 Entity를 조회할 수 있다.")
        void _willSuccess() {
            //given
            Long likeId = 1L;
            Member member = Member.builder().id(1L).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(likeId).trip(trip).member(member).build();

            memberRepository.save(member);
            tripRepository.save(trip);
            Like savedLike = likeRepository.save(like);

            //when
            Optional<Like> result = likeRepository.findById(likeId);

            //then
            assertNotNull(result);
            assertEquals(savedLike.getId(), result.get().getId());
            assertEquals(savedLike.getMember().getId(), result.get().getId());
            assertEquals(savedLike.getTrip().getId(), result.get().getId());
        }
    }

    @Nested
    @DisplayName("deleteById()는")
    class Context_deleteById {

        @Test
        @DisplayName("특정 id를 가진 좋아요 정보를 삭제할 수 있다.")
        void _willSuccess() {
            //given
            Member member = Member.builder().id(1L).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(1L).trip(trip).member(member).build();

            memberRepository.save(member);
            tripRepository.save(trip);
            Like savedLike = likeRepository.save(like);

            //when
            likeRepository.deleteById(1L);

            //then
            assertFalse(likeRepository.existsById(savedLike.getId()));
        }
    }
}
