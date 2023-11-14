package com.fc.toy_project3.domain.like.unit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.trip.entity.Trip;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class LikeRepositoryTest {

    @Mock
    private LikeRepository likeRepository;

    @Nested
    @DisplayName("findByMemberIdAndTripId()은")
    class Context_findByMemberIdAndTripId {

        @Test
        @DisplayName("특정 회원 id와 여행 id를 가진 좋아요 정보 Entity를 조회할 수 있다.")
        void _willSuccess() {
            //given
            Member member = Member.builder().id(1L).email("fc123@naver.com").nickname("닉1").password("1234").build();
            Trip trip = Trip.builder().id(1L).name("제주도 여행").startDate(LocalDate.of(2023, 10, 25))
                .endDate(LocalDate.of(2023, 10, 26)).isDomestic(true).itineraries(new ArrayList<>())
                .build();
            Like like = Like.builder().id(1L).trip(trip).member(member).build();
            given(likeRepository.findByMemberIdAndTripId(1L, 1l)).willReturn(like);

            // when
            Like result = likeRepository.findByMemberIdAndTripId(1L, 1L);

            //then
            assertEquals(like, result);
            verify(likeRepository, times(1)).findByMemberIdAndTripId(1L, 1L);
        }
    }
}
