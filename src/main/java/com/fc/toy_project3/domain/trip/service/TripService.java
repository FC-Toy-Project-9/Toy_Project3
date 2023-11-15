package com.fc.toy_project3.domain.trip.service;

import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.itinerary.exception.NotItineraryAuthorException;
import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.exception.InvalidTripDateRangeException;
import com.fc.toy_project3.domain.trip.exception.NotTripAuthorException;
import com.fc.toy_project3.domain.trip.exception.TripNotFoundException;
import com.fc.toy_project3.domain.trip.exception.WrongTripEndDateException;
import com.fc.toy_project3.domain.trip.exception.WrongTripStartDateException;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Trip Service
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final MemberService memberService;
    private final LikeRepository likeRepository;

    /**
     * 여행 정보 등록
     *
     * @param postTripRequestDTO 여행 정보 등록 요청 DTO
     * @return 여행 정보 응답 DTO
     */
    public TripResponseDTO postTrip(PostTripRequestDTO postTripRequestDTO, long memberId) {
        LocalDate startDate = DateTypeFormatterUtil.dateFormatter(
            postTripRequestDTO.getStartDate());
        LocalDate endDate = DateTypeFormatterUtil.dateFormatter(postTripRequestDTO.getEndDate());
        if (startDate.isAfter(endDate)) {
            throw new InvalidTripDateRangeException();
        }
        Member member = memberService.getMember(memberId);
        return new TripResponseDTO(tripRepository.save(Trip.builder()
            .member(member)
            .name(postTripRequestDTO.getTripName())
            .startDate(startDate)
            .endDate(endDate)
            .isDomestic(postTripRequestDTO.getIsDomestic())
            .build()));
    }

    /**
     * 여행 정보 목록 페이징 조회
     *
     * @param getTripsRequestDTO 여행 정보 목록 조회 조건이 담긴 요청 DTO
     * @param pageable 페이징 요청 정보
     * @return 여행 정보 응답 DTO 리스트
     */
    public GetTripsResponseDTO getTrips(GetTripsRequestDTO getTripsRequestDTO,
        Pageable pageable) {
        List<TripsResponseDTO> trips = new ArrayList<>();
        Page<Trip> tripList = tripRepository.findAllBySearchCondition(getTripsRequestDTO, pageable);
        tripList.forEach(trip -> trips.add(new TripsResponseDTO(trip)));
        return GetTripsResponseDTO.builder()
            .totalPages(tripList.getTotalPages())
            .isLastPage(tripList.isLast())
            .totalTrips(tripList.getTotalElements())
            .trips(trips)
            .build();
    }

    /**
     * 회원이 좋아요 한 여행 정보 목록 조회
     *
     * @param memberId 회원 ID
     * @param pageable 페이징 요청 정보
     * @return 여행 정보 응답 DTO 리스트
     */
    public GetTripsResponseDTO getLikedTrips(long memberId,  Pageable pageable){
        List<TripsResponseDTO> trips = new ArrayList<>();
        Page<Like> likeList = likeRepository.findAllByMemberIdAndPageable(memberId, pageable);
        likeList.forEach(like -> trips.add(new TripsResponseDTO(like.getTrip())));
        return GetTripsResponseDTO.builder()
            .totalPages(likeList.getTotalPages())
            .isLastPage(likeList.isLast())
            .totalTrips(likeList.getTotalElements())
            .trips(trips)
            .build();
    }

    /**
     * 특정 ID 값에 해당하는 여행 정보 조회
     *
     * @param id 조회할 하는 여행 ID
     * @return 여행 정보 응답 DTO
     */
    public GetTripResponseDTO getTripById(Long id) {
        return new GetTripResponseDTO(getTrip(id));
    }

    /**
     * 여행 정보 수정
     *
     * @param updateTripRequestDTO 여행 정보 수정 요청 DTO
     * @return 수정된 여행 정보 응답 DTO
     */
    public TripResponseDTO updateTrip(UpdateTripRequestDTO updateTripRequestDTO, Long memberId) {
        Trip trip = getTrip(updateTripRequestDTO.getTripId());
        isAuthor(trip, memberId);
        LocalDate startDate = updateTripRequestDTO.getStartDate() == null ? trip.getStartDate()
            : DateTypeFormatterUtil.dateFormatter(updateTripRequestDTO.getStartDate());
        LocalDate endDate = updateTripRequestDTO.getEndDate() == null ? trip.getEndDate()
            : DateTypeFormatterUtil.dateFormatter(updateTripRequestDTO.getEndDate());
        checkTripDate(trip, startDate, endDate);
        trip.updateTrip(updateTripRequestDTO);
        return new TripResponseDTO(trip);
    }

    /**
     * 특정 ID 값에 해당하는 여행 정보 Entity 조회
     *
     * @param id 조회할 하는 여행 ID
     * @return 여행 정보 Entity
     */
    public Trip getTrip(Long id) {
        return tripRepository.findById(id).orElseThrow(TripNotFoundException::new);
    }

    /**
     * 여행 일자 검증
     *
     * @param trip      여행 entity
     * @param startDate 여행 시작일
     * @param endDate   여행 종료일
     */
    private void checkTripDate(Trip trip, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(getMaxDate(trip.getItineraries())) || startDate.isAfter(endDate)) {
            throw new WrongTripStartDateException();
        }
        if (endDate.isBefore(getMinDate(trip.getItineraries()))) {
            throw new WrongTripEndDateException();
        }
    }

    /**
     * 알맞은 최대 여행 시작일 반환
     *
     * @param itineraries 여정 리스트
     * @return 알맞은 최소 여행 시작일
     */
    private LocalDate getMaxDate(List<Itinerary> itineraries) {
        LocalDate max = LocalDate.MAX;
        for (Itinerary itinerary : itineraries) {
            if (itinerary instanceof Accommodation accommodation) {
                if (accommodation.getCheckIn().toLocalDate().isBefore(max)) {
                    max = accommodation.getCheckIn().toLocalDate();
                }
            } else if (itinerary instanceof Transportation transportation) {
                if (transportation.getDepartureTime().toLocalDate().isBefore(max)) {
                    max = transportation.getDepartureTime().toLocalDate();
                }
            } else if (itinerary instanceof Visit visit) {
                if (visit.getArrivalTime().toLocalDate().isBefore(max)) {
                    max = visit.getArrivalTime().toLocalDate();
                }
            }
        }
        return max;
    }

    /**
     * 알맞은 최소 여행 종료일 반환
     *
     * @param itineraries 여정 리스트
     * @return 알맞은 최소 여행 시작일
     */
    private LocalDate getMinDate(List<Itinerary> itineraries) {
        LocalDate min = LocalDate.MIN;
        for (Itinerary itinerary : itineraries) {
            if (itinerary instanceof Accommodation accommodation) {
                if (accommodation.getCheckOut().toLocalDate().isAfter(min)) {
                    min = accommodation.getCheckOut().toLocalDate();
                }
            } else if (itinerary instanceof Transportation transportation) {
                if (transportation.getArrivalTime().toLocalDate().isAfter(min)) {
                    min = transportation.getArrivalTime().toLocalDate();
                }
            } else if (itinerary instanceof Visit visit) {
                if (visit.getDepartureTime().toLocalDate().isAfter(min)) {
                    min = visit.getDepartureTime().toLocalDate();
                }
            }
        }
        return min;
    }

    /**
     * 특정 ID 값에 해당하는 여행 정보 삭제
     *
     * @param tripId 삭제할 여행 ID
     */
    public TripResponseDTO deleteTripById(Long tripId, Long memberId) {
        Trip trip = getTrip(tripId);
        isAuthor(trip, memberId);
        trip.delete(LocalDateTime.now());
        return new TripResponseDTO(trip);
    }

    /**
     * Trip의 좋아요 개수 증감
     *
     * @param tripId     좋아요 개수 증감 할 여행 ID
     * @param isIncrease 좋아요 개수 증가 여부
     */
    public void like(long tripId, boolean isIncrease) {
        Trip trip = getTrip(tripId);
        trip.updateLikeCount(isIncrease);
    }

    public void isAuthor(Trip trip, long memberId){
        if(trip.getMember().getId() != memberId){
            throw new NotTripAuthorException();
        }
    }
}
