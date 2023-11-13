package com.fc.toy_project3.domain.itinerary.service;

import com.fc.toy_project3.domain.itinerary.dto.request.create.AccommodationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.create.TransportationCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.create.VisitCreateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.AccommodationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.TransportationUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.request.update.VisitUpdateRequestDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.AccommodationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItineraryDeleteResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.ItinerarySearchResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.TransportationResponseDTO;
import com.fc.toy_project3.domain.itinerary.dto.response.VisitResponseDTO;
import com.fc.toy_project3.domain.itinerary.entity.Accommodation;
import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import com.fc.toy_project3.domain.itinerary.entity.Transportation;
import com.fc.toy_project3.domain.itinerary.entity.Visit;
import com.fc.toy_project3.domain.itinerary.exception.InvalidItineraryException;
import com.fc.toy_project3.domain.itinerary.exception.ItineraryNotFoundException;
import com.fc.toy_project3.domain.itinerary.repository.AccommodationRepository;
import com.fc.toy_project3.domain.itinerary.repository.ItineraryRepository;
import com.fc.toy_project3.domain.itinerary.repository.TransportationRepository;
import com.fc.toy_project3.domain.itinerary.repository.VisitRepository;
import com.fc.toy_project3.domain.trip.entity.Trip;
import com.fc.toy_project3.domain.trip.service.TripService;
import com.fc.toy_project3.global.util.DateTypeFormatterUtil;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final AccommodationRepository accommodationRepository;
    private final TransportationRepository transportationRepository;
    private final VisitRepository visitRepository;
    private final TripService tripService;

    @Value("${kakao-api.api-url}")
    private String uri;

    @Value("${kakao-api.api-key}")
    private String key;

    private HttpEntity<String> httpEntity;

    /**
     * Kakao Open API [키워드 검색하기] 를 위한 httpEntity를 생성하는 메서드
     */
    @PostConstruct
    protected void init() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + key);
        httpEntity = new HttpEntity<>(headers);
    }

    /**
     * qeury를 통해 Kakao Open API [키워드 검색하기] 결과값을 생성하는 메서드
     *
     * @param query 키워드
     * @return 장소명, 도로명, 장소 url이 담긴 List
     */
    public List<ItinerarySearchResponseDTO> getPlaceByKeyword(String query) throws Exception {
        URI tmp = UriComponentsBuilder.fromHttpUrl(uri).queryParam("query", query)
            .queryParam("page", 5).encode(StandardCharsets.UTF_8).build().toUri();

        Assert.notNull(query, "query");
        ResponseEntity<String> response = new RestTemplate().exchange(tmp, HttpMethod.GET,
            httpEntity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody().toString());
        JSONArray jsonArray = jsonObject.getJSONArray("documents");
        List<ItinerarySearchResponseDTO> itinerarySearchList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            JSONObject documentObj = jsonArray.getJSONObject(i);
            itinerarySearchList.add(
                ItinerarySearchResponseDTO.builder().placeName(documentObj.getString("place_name"))
                    .roadAddressName(documentObj.getString("road_address_name"))
                    .placeUrl(documentObj.getString("place_url")).build());
        }
        return itinerarySearchList;
    }

    /**
     * 숙박과 관련된 여정을 생성합니다.
     *
     * @param accommodationCreateRequestDTO 숙박 여정 생성 요청 DTO
     * @return 생성된 숙박 여정 응답 DTO
     */
    public AccommodationResponseDTO createAccommodation(
        AccommodationCreateRequestDTO accommodationCreateRequestDTO) {
        Trip trip = tripService.getTrip(accommodationCreateRequestDTO.getTripId());
        LocalDateTime checkIn = DateTypeFormatterUtil.dateTimeFormatter(
            accommodationCreateRequestDTO.getCheckIn());
        LocalDateTime checkOut = DateTypeFormatterUtil.dateTimeFormatter(
            accommodationCreateRequestDTO.getCheckOut());
        checkAccommodationDate(trip, checkIn, checkOut);
        return new AccommodationResponseDTO(accommodationRepository.save(
            Accommodation.builder().trip(trip)
                .itineraryName(accommodationCreateRequestDTO.getItineraryName())
                .accommodationName(accommodationCreateRequestDTO.getAccommodationName())
                .accommodationRoadAddressName(
                    accommodationCreateRequestDTO.getAccommodationRoadAddressName())
                .checkIn(checkIn).checkOut(checkOut).build()));
    }

    /**
     * 이동과 관련된 여정을 생성합니다.
     *
     * @param transportationCreateRequestDTO 이동 여정 생성 요청 DTO
     * @return 생성된 이동 여정 응답 DTO
     */
    public TransportationResponseDTO createTransportation(
        TransportationCreateRequestDTO transportationCreateRequestDTO) {
        Trip trip = tripService.getTrip(transportationCreateRequestDTO.getTripId());
        LocalDateTime departureTime = DateTypeFormatterUtil.dateTimeFormatter(
            transportationCreateRequestDTO.getDepartureTime());
        LocalDateTime arrivalTime = DateTypeFormatterUtil.dateTimeFormatter(
            transportationCreateRequestDTO.getArrivalTime());
        checkTransportationDate(trip, departureTime, arrivalTime);
        return new TransportationResponseDTO(transportationRepository.save(
            Transportation.builder().trip(trip)
                .itineraryName(transportationCreateRequestDTO.getItineraryName())
                .transportation(transportationCreateRequestDTO.getTransportation())
                .departurePlace(transportationCreateRequestDTO.getDeparturePlace())
                .departurePlaceRoadAddressName(
                    transportationCreateRequestDTO.getDeparturePlaceRoadAddressName())
                .destination(transportationCreateRequestDTO.getDestination())
                .destinationRoadAddressName(
                    transportationCreateRequestDTO.getDestinationRoadAddressName())
                .arrivalTime(arrivalTime).departureTime(departureTime).build()));
    }

    /**
     * 체류와 관련된 여정을 생성합니다.
     *
     * @param visitCreateRequestDTO 방문 여정 생성 요청 DTO
     * @return 생성된 방문 여정 응답 DTO
     */
    public VisitResponseDTO createVisit(VisitCreateRequestDTO visitCreateRequestDTO) {
        Trip trip = tripService.getTrip(visitCreateRequestDTO.getTripId());
        LocalDateTime departureTime = DateTypeFormatterUtil.dateTimeFormatter(
            visitCreateRequestDTO.getDepartureTime());
        LocalDateTime arrivalTime = DateTypeFormatterUtil.dateTimeFormatter(
            visitCreateRequestDTO.getArrivalTime());
        checkVisitDate(trip, departureTime, arrivalTime);
        return new VisitResponseDTO(visitRepository.save(
            Visit.builder().trip(trip).itineraryName(visitCreateRequestDTO.getItineraryName())
                .placeName(visitCreateRequestDTO.getPlaceName())
                .placeRoadAddressName(visitCreateRequestDTO.getPlaceRoadAddressName())
                .arrivalTime(arrivalTime).departureTime(departureTime).build()));
    }

    /**
     * 숙박과 관련된 여정을 수정합니다.
     *
     * @param accommodationUpdateRequestDTO 숙박 여정 수정 요청 DTO
     * @return 수정된 숙박 여정 응답 DTO
     */
    public AccommodationResponseDTO updateAccommodation(
        AccommodationUpdateRequestDTO accommodationUpdateRequestDTO) {
        Accommodation accommodation = (Accommodation) getItinerary(
            accommodationUpdateRequestDTO.getItineraryId());
        LocalDateTime checkIn = DateTypeFormatterUtil.dateTimeFormatter(
            accommodationUpdateRequestDTO.getCheckIn());
        LocalDateTime checkOut = DateTypeFormatterUtil.dateTimeFormatter(
            accommodationUpdateRequestDTO.getCheckOut());
        checkAccommodationDate(accommodation.getTrip(), checkIn, checkOut);
        accommodation.updateAccommodationInfo(accommodationUpdateRequestDTO.getItineraryName(),
            accommodationUpdateRequestDTO.getAccommodationName(),
            accommodationUpdateRequestDTO.getAccommodationRoadAddressName(), checkIn, checkOut);
        return new AccommodationResponseDTO(accommodation);
    }

    /**
     * 이동과 관련된 여정을 수정합니다.
     *
     * @param transportationUpdateRequestDTO 이동 여정 수정 요청 DTO
     * @return 수정된 이동 여정 응답 DTO
     */
    public TransportationResponseDTO updateTransportation(
        TransportationUpdateRequestDTO transportationUpdateRequestDTO) {
        Transportation transportation = (Transportation) getItinerary(
            transportationUpdateRequestDTO.getItineraryId());
        LocalDateTime departureTime = DateTypeFormatterUtil.dateTimeFormatter(
            transportationUpdateRequestDTO.getDepartureTime());
        LocalDateTime arrivalTime = DateTypeFormatterUtil.dateTimeFormatter(
            transportationUpdateRequestDTO.getArrivalTime());
        checkTransportationDate(transportation.getTrip(), departureTime, arrivalTime);
        transportation.updateTransportationInfo(transportationUpdateRequestDTO.getItineraryName(),
            transportationUpdateRequestDTO.getTransportation(),
            transportationUpdateRequestDTO.getDeparturePlace(),
            transportationUpdateRequestDTO.getDeparturePlaceRoadAddressName(),
            transportationUpdateRequestDTO.getDestination(),
            transportationUpdateRequestDTO.getDestinationRoadAddressName(), departureTime,
            arrivalTime);
        return new TransportationResponseDTO(transportation);
    }

    /**
     * 체류와 관련된 여정을 수정합니다.
     *
     * @param visitUpdateRequestDTO 방문 여정 수정 요청 DTO
     * @return 수정된 방문 여정 응답 DTO
     */
    public VisitResponseDTO updateVisit(VisitUpdateRequestDTO visitUpdateRequestDTO) {
        Visit visit = (Visit) getItinerary(visitUpdateRequestDTO.getItineraryId());
        LocalDateTime departureTime = DateTypeFormatterUtil.dateTimeFormatter(
            visitUpdateRequestDTO.getDepartureTime());
        LocalDateTime arrivalTime = DateTypeFormatterUtil.dateTimeFormatter(
            visitUpdateRequestDTO.getArrivalTime());
        checkVisitDate(visit.getTrip(), departureTime, arrivalTime);
        visit.updateVisitInfo(visitUpdateRequestDTO.getItineraryName(),
            visitUpdateRequestDTO.getPlaceName(), visitUpdateRequestDTO.getPlaceRoadAddressName(),
            departureTime, arrivalTime);
        return new VisitResponseDTO(visit);
    }

    /**
     * itineraryId를 통해 여정정보를 삭제하는 메서드
     *
     * @param itineraryId 여정 Id
     * @return 삭제된 여정 정보
     */
    public ItineraryDeleteResponseDTO deleteItinerary(Long itineraryId) {
        Itinerary itinerary = getItinerary(itineraryId);
        itineraryRepository.delete(itinerary);
        ItineraryDeleteResponseDTO itineraryDeleteResponseDTO = ItineraryDeleteResponseDTO.builder()
            .itineraryId(itinerary.getId()).build();
        return itineraryDeleteResponseDTO;
    }

    private Itinerary getItinerary(Long itineraryId) {
        return itineraryRepository.findById(itineraryId)
            .orElseThrow(ItineraryNotFoundException::new);
    }

    /**
     * 숙박 여정의 날짜 유효성 검사
     *
     * @param trip     여정이 속한 여행
     * @param checkIn  숙박 시작일
     * @param checkOut 숙박 종료일
     * @throws InvalidItineraryException 날짜 유효성 검사 실패 시 발생
     */
    private void checkAccommodationDate(Trip trip, LocalDateTime checkIn, LocalDateTime checkOut) {
        LocalDateTime tripStartDateTime = trip.getStartDate().atStartOfDay();
        LocalDateTime tripEndDateTime = trip.getEndDate().atTime(LocalTime.MAX);
        if (checkIn.isAfter(checkOut)) {
            throw new InvalidItineraryException("체크인 시간은 체크아웃 시간보다 이른 시간이어야 합니다.");
        }
        if (checkIn.isBefore(tripStartDateTime)) {
            throw new InvalidItineraryException("체크인 시간은 여행 시작일 이후여야 합니다.");
        }
        if (checkIn.isAfter(tripEndDateTime)) {
            throw new InvalidItineraryException("체크인 시간은 여행 종료일보다 빠른 시간이어야 합니다.");
        }
        if (checkOut.isAfter(tripEndDateTime)) {
            throw new InvalidItineraryException("체크아웃 시간은 여행 종료일보다 빠른 시간이어야 합니다.");
        }
    }

    /**
     * 이동에 관한 날짜 유효성 검사
     *
     * @param trip          여정이 속한 여행
     * @param departureTime 출발 시간
     * @param arrivalTime   도착 시간
     * @throws InvalidItineraryException 날짜 유효성 검사 실패 시 발생
     */
    private void checkTransportationDate(Trip trip, LocalDateTime departureTime,
        LocalDateTime arrivalTime) {
        LocalDateTime tripStartDateTime = trip.getStartDate().atStartOfDay();
        LocalDateTime tripEndDateTime = trip.getEndDate().atTime(LocalTime.MAX);
        if (departureTime.isAfter(arrivalTime)) {
            throw new InvalidItineraryException("출발 시간은 도착 시간보다 이른 시간이어야 합니다.");
        }
        if (departureTime.isBefore(tripStartDateTime)) {
            throw new InvalidItineraryException("출발 시간은 여행 시작일 이후여야 합니다.");
        }
        if (departureTime.isAfter(tripEndDateTime)) {
            throw new InvalidItineraryException("출발 시간은 여행 종료일보다 빠른 시간이어야 합니다.");
        }
        if (arrivalTime.isAfter(tripEndDateTime)) {
            throw new InvalidItineraryException("도착 시간은 여행 종료일보다 빠른 시간이어야 합니다.");
        }
    }

    /**
     * 체류에 관한 날짜 유효성 검사
     *
     * @param trip          여정이 속한 여행
     * @param departureTime 출발 시간
     * @param arrivalTime   도착 시간
     * @throws InvalidItineraryException 날짜 유효성 검사 실패 시 발생
     */
    private void checkVisitDate(Trip trip, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        LocalDateTime tripStartDateTime = trip.getStartDate().atStartOfDay();
        LocalDateTime tripEndDateTime = trip.getEndDate().atTime(LocalTime.MAX);
        if (arrivalTime.isAfter(departureTime)) {
            throw new InvalidItineraryException("도착 시간은 출발 시간보다 이른 시간이어야 합니다.");
        }
        if (arrivalTime.isBefore(tripStartDateTime)) {
            throw new InvalidItineraryException("도착 시간은 여행 시작일 이후여야 합니다.");
        }
        if (arrivalTime.isAfter(tripEndDateTime)) {
            throw new InvalidItineraryException("도착 시간은 여행 종료일보다 빠른 시간이어야 합니다.");
        }
        if (departureTime.isAfter(tripEndDateTime)) {
            throw new InvalidItineraryException("출발 시간은 여행 종료일보다 빠른 시간이어야 합니다.");
        }
    }
}
