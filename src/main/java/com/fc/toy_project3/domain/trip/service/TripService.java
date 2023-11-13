package com.fc.toy_project3.domain.trip.service;

import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.exception.InvalidTripDateRangeException;
import com.fc.toy_project3.domain.trip.exception.TripNotFoundException;
import com.fc.toy_project3.domain.trip.exception.WrongTripEndDateException;
import com.fc.toy_project3.domain.trip.exception.WrongTripStartDateException;
import com.fc.toy_project3.domain.trip.repository.TripRepository;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    /**
     * 여행 정보 등록
     *
     * @param postTripRequestDTO 여행 정보 등록 요청 DTO
     * @return 여행 정보 응답 DTO
     */
    public TripResponseDTO postTrip(PostTripRequestDTO postTripRequestDTO) {

        LocalDate startDate = DateTypeFormatterUtil.dateFormatter(
            postTripRequestDTO.getStartDate());
        LocalDate endDate = DateTypeFormatterUtil.dateFormatter(postTripRequestDTO.getEndDate());
        if (startDate.isAfter(endDate)) {
            throw new InvalidTripDateRangeException();
        }
        Trip trip = Trip.builder()
            .name(postTripRequestDTO.getTripName())
            .startDate(startDate)
            .endDate(endDate)
            .isDomestic(postTripRequestDTO.getIsDomestic())
            .build();
        return new TripResponseDTO(tripRepository.save(trip));
    }

    /**
     * 여행 정보 목록 조회
     *
     * @return 여행 정보 응답 DTO 리스트
     */
    public List<GetTripsResponseDTO> getTrips() {
        List<GetTripsResponseDTO> trips = new ArrayList<>();
        List<Trip> tripList = tripRepository.findAll(Sort.by(Direction.ASC, "id"));
        for (Trip trip : tripList) {
            trips.add(new GetTripsResponseDTO(trip));
        }
        return trips;
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

    public TripResponseDTO updateTrip(UpdateTripRequestDTO updateTripRequestDTO) {
        Trip trip = getTrip(updateTripRequestDTO.getTripId());
        checkTripDate(trip,
            DateTypeFormatterUtil.dateFormatter(updateTripRequestDTO.getStartDate()),
            DateTypeFormatterUtil.dateFormatter(updateTripRequestDTO.getEndDate()));
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
    public void deleteTripById(Long tripId) {
        Trip trip = getTrip(tripId);
        tripRepository.delete(trip);
    }
}
