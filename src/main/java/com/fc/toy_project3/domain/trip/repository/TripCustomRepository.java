package com.fc.toy_project3.domain.trip.repository;

import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripCustomRepository {

    Page<Trip> findAllBySearchCondition(GetTripsRequestDTO getTripsRequestDTO,
                                        Pageable pageable);
}