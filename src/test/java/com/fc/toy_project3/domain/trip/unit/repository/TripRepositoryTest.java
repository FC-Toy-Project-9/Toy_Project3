package com.fc.toy_project3.domain.trip.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.toy_project3.config.TestJpaConfig;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.TripPageRequestDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({TestJpaConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TripRepositoryTest {

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
        tripRepository.deleteAll();
        memberRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE trip").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE member").executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE trip ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void saveTripList() {
        Member member1 = Member.builder().nickname("닉네임1").build();
        Member member2 = Member.builder().nickname("닉네임2").build();
        Member member3 = Member.builder().nickname("닉네임3").build();
        memberRepository.saveAll(List.of(member1, member2, member3));
        Trip trip1 = Trip.builder()
            .member(member1)
            .name("제주도 여행")
            .startDate(LocalDate.of(2023, 10, 25))
            .endDate(LocalDate.of(2023, 10, 26))
            .isDomestic(true)
            .likeCount(0L)
            .itineraries(new ArrayList<>())
            .build();
        Trip trip2 = Trip.builder()
            .member(member2)
            .name("속초 겨울바다 여행")
            .startDate(LocalDate.of(2023, 11, 27))
            .endDate(LocalDate.of(2023, 11, 29))
            .isDomestic(true)
            .likeCount(10L)
            .itineraries(new ArrayList<>())
            .build();
        Trip trip3 = Trip.builder()
            .member(member3)
            .name("크리스마스 미국 여행")
            .startDate(LocalDate.of(2023, 12, 24))
            .endDate(LocalDate.of(2023, 12, 26))
            .isDomestic(false)
            .likeCount(3L)
            .itineraries(new ArrayList<>())
            .build();
        Trip trip4 = Trip.builder()
            .member(member2)
            .name("제주도 가족 여행")
            .startDate(LocalDate.of(2023, 11, 17))
            .endDate(LocalDate.of(2023, 11, 19))
            .isDomestic(true)
            .likeCount(7L)
            .itineraries(new ArrayList<>())
            .build();
        tripRepository.saveAll(List.of(trip1, trip2, trip3, trip4));
    }

    @Nested
    @DisplayName("save()는")
    class Context_save {

        @Test
        @DisplayName("여행 정보를 저장할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder().nickname("닉네임1").build();
            memberRepository.save(member);
            Trip trip = Trip.builder()
                .member(member)
                .name("제주도 여행")
                .startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26))
                .isDomestic(true)
                .likeCount(0L)
                .itineraries(new ArrayList<>())
                .build();

            // when
            Trip result = tripRepository.save(trip);

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getMember().getNickname()).isEqualTo("닉네임1");
            assertThat(result.getName()).isEqualTo("제주도 여행");
            assertThat(result.getStartDate()).isNotNull();
            assertThat(result.getEndDate()).isNotNull();
            assertThat(result.getIsDomestic()).isTrue();
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getItineraries()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById()는")
    class Context_findById {

        @Test
        @DisplayName("여행 ID로 여행 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            saveTripList();
            long tripId = 1L;

            // when
            Optional<Trip> result = tripRepository.findById(tripId);

            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getId()).isNotNull();
            assertThat(result.get().getMember().getNickname()).isEqualTo("닉네임1");
            assertThat(result.get().getName()).isEqualTo("제주도 여행");
            assertThat(result.get().getStartDate()).isNotNull();
            assertThat(result.get().getEndDate()).isNotNull();
            assertThat(result.get().getIsDomestic()).isTrue();
            assertThat(result.get().getLikeCount()).isEqualTo(0L);
            assertThat(result.get().getCreatedAt()).isNotNull();
            assertThat(result.get().getItineraries()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllBySearchCondition()은")
    class Context_findAllBySearchCondition {

        @Test
        @DisplayName("여행 이름으로 여행 목록을 조회할 수 있다.")
        void tripName_willSuccess() {
            // given
            saveTripList();
            GetTripsRequestDTO getTripsRequestDTO = GetTripsRequestDTO.builder()
                .tripName("제주도")
                .build();
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("createdAt")
                .sort("ASC")
                .build().of();

            // when
            Page<Trip> result = tripRepository.findAllBySearchCondition(getTripsRequestDTO,
                pageable);

            // then
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.isLast()).isTrue();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.get().toList()).isNotEmpty();
            assertThat(result.get().toList().get(0).getName()).isEqualTo("제주도 여행");
            assertThat(result.get().toList().get(1).getName()).isEqualTo("제주도 가족 여행");
        }

        @Test
        @DisplayName("여행 이름으로 여행 목록을 조회할 수 있다.")
        void nickname_willSuccess() {
            // given
            saveTripList();
            GetTripsRequestDTO getTripsRequestDTO = GetTripsRequestDTO.builder()
                .nickname("닉네임2")
                .build();
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("createdAt")
                .sort("ASC")
                .build().of();

            // when
            Page<Trip> result = tripRepository.findAllBySearchCondition(getTripsRequestDTO,
                pageable);

            // then
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.isLast()).isTrue();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.get().toList()).isNotEmpty();
            assertThat(result.get().toList().get(0).getMember().getNickname()).isEqualTo("닉네임2");
            assertThat(result.get().toList().get(1).getMember().getNickname()).isEqualTo("닉네임2");
        }

        @Test
        @DisplayName("최신순으로 여행 목록을 정렬하여 조회할 수 있다.")
        void orderByCreatedAt_willSuccess() {
            // given
            saveTripList();
            GetTripsRequestDTO getTripsRequestDTO = GetTripsRequestDTO.builder()
                .build();
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("createdAt")
                .sort("DESC")
                .build().of();

            // when
            Page<Trip> result = tripRepository.findAllBySearchCondition(getTripsRequestDTO,
                pageable);

            // then
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.isLast()).isTrue();
            assertThat(result.getTotalElements()).isEqualTo(4L);
            assertThat(result.get().toList()).isNotEmpty();
            assertThat(result.get().toList().get(0).getName()).isEqualTo("제주도 가족 여행");
            assertThat(result.get().toList().get(1).getName()).isEqualTo("크리스마스 미국 여행");
            assertThat(result.get().toList().get(2).getName()).isEqualTo("속초 겨울바다 여행");
            assertThat(result.get().toList().get(3).getName()).isEqualTo("제주도 여행");
        }

        @Test
        @DisplayName("좋아요순으로 여행 목록을 정렬하여 조회할 수 있다.")
        void orderByLikeCount_willSuccess() {
            // given
            saveTripList();
            GetTripsRequestDTO getTripsRequestDTO = GetTripsRequestDTO.builder()
                .build();
            Pageable pageable = TripPageRequestDTO.builder()
                .page(0)
                .size(10)
                .criteria("likeCount")
                .sort("DESC")
                .build().of();

            // when
            Page<Trip> result = tripRepository.findAllBySearchCondition(getTripsRequestDTO,
                pageable);

            // then
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.isLast()).isTrue();
            assertThat(result.getTotalElements()).isEqualTo(4);
            assertThat(result.get().toList()).isNotEmpty();
            assertThat(result.get().toList().get(0).getLikeCount()).isEqualTo(10L);
            assertThat(result.get().toList().get(1).getLikeCount()).isEqualTo(7L);
            assertThat(result.get().toList().get(2).getLikeCount()).isEqualTo(3L);
            assertThat(result.get().toList().get(3).getLikeCount()).isEqualTo(0L);
        }
    }
}
