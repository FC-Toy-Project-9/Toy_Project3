package com.fc.toy_project3.domain.itinerary.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.toy_project3.config.TestJpaConfig;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.itinerary.repository.AccommodationRepository;
import com.fc.toy_project3.domain.itinerary.repository.ItineraryRepository;
import com.fc.toy_project3.domain.itinerary.repository.TransportationRepository;
import com.fc.toy_project3.domain.itinerary.repository.VisitRepository;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

@DataJpaTest
@Import({TestJpaConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ItineraryRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private TransportationRepository transportationRepository;

    @Autowired
    private VisitRepository visitRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        tripRepository.deleteAll();
        memberRepository.deleteAll();
        itineraryRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE trip").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE member").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE itinerary").executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE member ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE trip ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager
            .createNativeQuery("ALTER TABLE itinerary ALTER COLUMN `id` RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Nested
    @DisplayName("save는 ")
    class Context_save {

        @Test
        @DisplayName("숙박 여정 정보를 저장할 수 있다.")
        void accommodation_willSuccess() {
            // given
            Accommodation accommodation = Accommodation.builder()
                .trip(saveTrip())
                .name("제주여정1")
                .accommodationName("제주신라호텔")
                .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .checkIn(LocalDateTime.of(2023, 10, 25, 15, 0))
                .checkOut(LocalDateTime.of(2023, 10, 26, 11, 0))
                .build();

            // when
            Accommodation result = accommodationRepository.save(accommodation);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTrip().getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("제주여정1");
            assertThat(result.getAccommodationName()).isEqualTo("제주신라호텔");
            assertThat(result.getAccommodationRoadAddressName()).isEqualTo("제주 서귀포시 중문관광로72번길 75");
            assertThat(result.getCheckIn()).isEqualTo(LocalDateTime.of(2023, 10, 25, 15, 0));
            assertThat(result.getCheckOut()).isEqualTo(LocalDateTime.of(2023, 10, 26, 11, 0));
            assertThat(result.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("이동 여정 정보를 저장할 수 있다.")
        void transportation_willSuccess() {
            // given
            Transportation transportation = Transportation.builder()
                .trip(saveTrip())
                .itineraryName("제주여정2")
                .transportation("카카오택시")
                .departurePlace("제주신라호텔")
                .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
                .destination("오설록 티 뮤지엄")
                .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
                .departureTime(LocalDateTime.of(2023, 10, 26, 12, 0))
                .arrivalTime(LocalDateTime.of(2023, 10, 26, 13, 0))
                .build();

            // when
            Transportation result = transportationRepository.save(transportation);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTrip().getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("제주여정2");
            assertThat(result.getTransportation()).isEqualTo("카카오택시");
            assertThat(result.getDeparturePlace()).isEqualTo("제주신라호텔");
            assertThat(result.getDeparturePlaceRoadAddressName()).isEqualTo("제주 서귀포시 중문관광로72번길 75");
            assertThat(result.getDestination()).isEqualTo("오설록 티 뮤지엄");
            assertThat(result.getDestinationRoadAddressName()).isEqualTo(
                "제주 서귀포시 안덕면 신화역사로 15 오설록");
            assertThat(result.getDepartureTime()).isEqualTo(LocalDateTime.of(2023, 10, 26, 12, 0));
            assertThat(result.getArrivalTime()).isEqualTo(LocalDateTime.of(2023, 10, 26, 13, 0));
            assertThat(result.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("체류 여정 정보를 저장할 수 있다.")
        void visit_willSuccess() {
            // given
            Visit visit = Visit.builder()
                .trip(saveTrip())
                .itineraryName("제주여정3")
                .placeName("카멜리아힐")
                .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
                .arrivalTime(LocalDateTime.of(2023, 10, 26, 14, 0))
                .departureTime(LocalDateTime.of(2023, 10, 26, 16, 0))
                .build();

            // when
            Visit result = visitRepository.save(visit);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTrip().getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("제주여정3");
            assertThat(result.getPlaceName()).isEqualTo("카멜리아힐");
            assertThat(result.getPlaceRoadAddressName()).isEqualTo("제주 서귀포시 안덕면 병악로 166");
            assertThat(result.getArrivalTime()).isEqualTo(LocalDateTime.of(2023, 10, 26, 14, 0));
            assertThat(result.getDepartureTime()).isEqualTo(LocalDateTime.of(2023, 10, 26, 16, 0));
            assertThat(result.getCreatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("findByIdAndDeletedAt()은 ")
    class Context_findByIdAndDeletedAt {

        @Test
        @DisplayName("삭제되지 않은 여정 정보를 불러올 수 있다.")
        void _willSuccess() {
            // given
            saveItineraryList();
            long itineraryId = 3L;

            // when
            Optional<Itinerary> result = itineraryRepository.findByIdAndDeletedAt(itineraryId,
                null);
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        @DisplayName("삭제된 여정 정보는 불러올 수 없다.")
        void _willFail() {
            // given
            saveItineraryList();
            long itineraryId = 3L;
            Optional<Itinerary> itinerary = itineraryRepository.findById(3L);
            assertThat(itinerary.isPresent()).isTrue();
            itinerary.get().delete(LocalDateTime.now());

            // when
            Optional<Itinerary> result = itineraryRepository.findByIdAndDeletedAt(itineraryId,
                null);
            assertThat(result.isPresent()).isFalse();
        }
    }

    private Trip saveTrip() {
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
        return tripRepository.save(trip);
    }

    private void saveItineraryList() {
        Trip trip = saveTrip();
        Accommodation accommodation = Accommodation.builder()
            .trip(trip)
            .name("제주여정1")
            .accommodationName("제주신라호텔")
            .accommodationRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .checkIn(LocalDateTime.of(2023, 10, 25, 15, 0))
            .checkOut(LocalDateTime.of(2023, 10, 26, 11, 0))
            .build();
        accommodationRepository.save(accommodation);
        Transportation transportation = Transportation.builder()
            .id(2L)
            .trip(trip)
            .itineraryName("제주여정2")
            .transportation("카카오택시")
            .departurePlace("제주신라호텔")
            .departurePlaceRoadAddressName("제주 서귀포시 중문관광로72번길 75")
            .destination("오설록 티 뮤지엄")
            .destinationRoadAddressName("제주 서귀포시 안덕면 신화역사로 15 오설록")
            .departureTime(LocalDateTime.of(2023, 10, 26, 12, 0))
            .arrivalTime(LocalDateTime.of(2023, 10, 26, 13, 0))
            .build();
        transportationRepository.save(transportation);
        Visit visit = Visit.builder()
            .id(3L)
            .trip(trip)
            .itineraryName("제주여정3")
            .placeName("카멜리아힐")
            .placeRoadAddressName("제주 서귀포시 안덕면 병악로 166")
            .arrivalTime(LocalDateTime.of(2023, 10, 26, 14, 0))
            .departureTime(LocalDateTime.of(2023, 10, 26, 16, 0))
            .build();
        visitRepository.save(visit);
    }
}
