package com.fc.toy_project3.domain.trip.controller;

import com.fc.toy_project3.domain.trip.dto.request.GetTripsRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.TripPageRequestDTO;
import com.fc.toy_project3.domain.trip.dto.request.UpdateTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.GetTripsResponseDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.service.TripService;
import com.fc.toy_project3.global.common.ResponseDTO;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Trip REST Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trips")
public class TripRestController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<ResponseDTO<TripResponseDTO>> postTrip(
        @Valid @RequestBody PostTripRequestDTO postTripRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDTO.res(HttpStatus.CREATED,
                tripService.postTrip(postTripRequestDTO, customUserDetails.getMemberId()),
                "성공적으로 여행 정보를 등록했습니다."));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<GetTripsResponseDTO>> getTrips(
        @RequestParam(required = false) String tripName,
        @RequestParam(required = false) String nickname,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "createdAt") String orderBy,
        @RequestParam(defaultValue = "DESC") String sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, tripService.getTrips(
                GetTripsRequestDTO.builder()
                    .tripName(tripName)
                    .nickname(nickname)
                    .build(),
                TripPageRequestDTO.builder()
                    .page(page)
                    .size(pageSize)
                    .criteria(orderBy)
                    .sort(sort)
                    .build().of()), "성공적으로 여행 정보 목록을 조회했습니다."));
    }

    @GetMapping("/likes")
    public ResponseEntity<ResponseDTO<GetTripsResponseDTO>> getLikedTrip(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "createdAt") String orderBy,
        @RequestParam(defaultValue = "DESC") String sort,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK,
                tripService.getLikedTrips(customUserDetails.getMemberId(),
                    TripPageRequestDTO.builder()
                        .page(page)
                        .size(pageSize)
                        .criteria(orderBy)
                        .sort(sort)
                        .build().of()),
                "성공적으로 회원이 좋아요한 여행 정보 목록을 조회했습니다."));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<ResponseDTO<GetTripResponseDTO>> getTripById(@PathVariable long tripId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, tripService.getTripById(tripId),
                "성공적으로 여행 정보를 조회했습니다."));
    }

    @PatchMapping
    public ResponseEntity<ResponseDTO<TripResponseDTO>> updateTrip(
        @Valid @RequestBody UpdateTripRequestDTO updateTripRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, tripService.updateTrip(updateTripRequestDTO,
                    customUserDetails.getMemberId()),
                "성공적으로 여행 정보를 수정했습니다."));
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<ResponseDTO<TripResponseDTO>> deleteTripById(@PathVariable long tripId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, tripService.deleteTripById(tripId,
                    customUserDetails.getMemberId()),
                "성공적으로 여행 정보를 삭제했습니다."));
    }
}
