package com.fc.toy_project3.domain.itinerary.controller;

import com.fc.toy_project3.domain.itinerary.dto.request.create.AccommodationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.create.TransportationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.create.VisitCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.AccommodationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.TransportationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.VisitUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItinerarySearchResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.service.ItineraryService;
import com.fc.toy_project3.global.common.ResponseDTO;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itineraries")
public class ItineraryRestController {

    private final ItineraryService itineraryService;

    @GetMapping("/keyword/{query}")
    public ResponseEntity<ResponseDTO<List<ItinerarySearchResponseDTO>>> getPlaceByKeyword(
        @PathVariable String query) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, itineraryService.getPlaceByKeyword(query),
                "성공적으로 키워드로 장소를 조회했습니다."));

    }

    @PostMapping("/accommodations")
    public ResponseEntity<ResponseDTO<AccommodationResponseDTO>> createAccommodation(
        @Valid @RequestBody AccommodationCreateRequestDTO accommodationCreateRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.res(HttpStatus.CREATED,
            itineraryService.createAccommodation(accommodationCreateRequestDTO,
                customUserDetails.getMemberId()), "숙박 여정을 성공적으로 등록했습니다."));
    }

    @PostMapping("/transportations")
    public ResponseEntity<ResponseDTO<TransportationResponseDTO>> createTransportation(
        @Valid @RequestBody TransportationCreateRequestDTO transportationCreateRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.res(HttpStatus.CREATED,
            itineraryService.createTransportation(transportationCreateRequestDTO,
                customUserDetails.getMemberId()), "이동 여정을 성공적으로 등록했습니다."));
    }

    @PostMapping("/visits")
    public ResponseEntity<ResponseDTO<VisitResponseDTO>> createVisit(
        @Valid @RequestBody VisitCreateRequestDTO visitCreateRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDTO.res(HttpStatus.CREATED, itineraryService.createVisit(visitCreateRequestDTO,
                customUserDetails.getMemberId()), "체류 여정을 성공적으로 등록했습니다."));
    }

    @PatchMapping("/accommodations")
    public ResponseEntity<ResponseDTO<AccommodationResponseDTO>> updateAccommodation(
        @Valid @RequestBody AccommodationUpdateRequestDTO accommodationUpdateRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.res(HttpStatus.OK,
            itineraryService.updateAccommodation(accommodationUpdateRequestDTO,
                customUserDetails.getMemberId()), "숙박 여정을 성공적으로 수정했습니다."));
    }

    @PatchMapping("/transportations")
    public ResponseEntity<ResponseDTO<TransportationResponseDTO>> updateTransportation(
        @Valid @RequestBody TransportationUpdateRequestDTO transportationUpdateRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.res(HttpStatus.OK,
            itineraryService.updateTransportation(transportationUpdateRequestDTO,
                customUserDetails.getMemberId()), "이동 여정을 성공적으로 수정했습니다."));
    }

    @PatchMapping("/visits")
    public ResponseEntity<ResponseDTO<VisitResponseDTO>> updateVisit(
        @Valid @RequestBody VisitUpdateRequestDTO visitUpdateRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, itineraryService.updateVisit(visitUpdateRequestDTO,
                customUserDetails.getMemberId()), "체류 여정을 성공적으로 수정했습니다."));
    }

    @DeleteMapping("/{itineraryId}")
    public ResponseEntity<ResponseDTO<Object>> deleteItinerary(
        @PathVariable long itineraryId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, itineraryService.deleteItinerary(itineraryId,
                customUserDetails.getMemberId()), "성공적으로 여정을 삭제했습니다."));
    }
}
