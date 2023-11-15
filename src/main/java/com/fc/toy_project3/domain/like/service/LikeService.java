package com.fc.toy_project3.domain.like.service;

import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.exception.LikeNotFoundException;
import com.fc.toy_project3.domain.like.exception.LikeUnauthorizedException;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Like Service
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final TripService tripService;
    private final LikeRepository likeRepository;
    private final MemberService memberService;

    /**
     * 좋아요 정보 등록
     *
     * @param memberId 회원 ID
     * @param likeRequestDTO 좋아요 등록 요청 DTO
     * @return 등록한 좋아요 정보 응답 DTO
     */
    public LikeResponseDTO createLike(Long memberId, LikeRequestDTO likeRequestDTO) {
        Long tripId = likeRequestDTO.getTripId();
        Like like = Like.builder().member(memberService.getMember(memberId)).trip(tripService.getTrip(tripId)).build();
        tripService.like(tripId, true);
        return new LikeResponseDTO(likeRepository.save(like));
    }


    /**
     * 특정 회원 ID, 여행 ID 값을 가지는 좋아요 정보 조회
     *
     * @param memberId 회원 ID
     * @param tripId 여행 ID
     * @return 좋아요 정보 응답 DTO
     */
    public LikeResponseDTO getLikeByMemberIdAndTripId(Long memberId, Long tripId){
        return new LikeResponseDTO(likeRepository.findByMemberIdAndTripId(memberId, tripId).orElseThrow(LikeNotFoundException::new));
    }

    /**
     * 특정 ID값에 해당하는 좋아요 정보 삭제
     * @param likeId lIKE
     * @return 삭제한 좋아요 정보 응답 DTO
     */
    public LikeResponseDTO deleteLikeById(Long memberId, Long likeId){
        Like like = likeRepository.findById(likeId).orElseThrow(LikeNotFoundException::new);
        if(!like.getMember().getId().equals(memberId)){
            throw new LikeUnauthorizedException();
        }
        likeRepository.deleteById(likeId);
        tripService.like(like.getTrip().getId(), false);
        return new LikeResponseDTO(like);
    }
}