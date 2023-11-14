package com.fc.toy_project3.domain.like.service;

import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.entity.Like;
import com.fc.toy_project3.domain.like.exception.LikeNotFoundException;
import com.fc.toy_project3.domain.like.repository.LikeRepository;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    /**
     * 좋아요 정보 등록
     *
     * @param memberId 회원 ID
     * @param likeRequestDTO 좋아요 등록 요청 DTO
     * @return 좋아요 정보 응답 DTO
     */
    public LikeResponseDTO createLike(Long memberId, LikeRequestDTO likeRequestDTO) {
        //getMember(), MemberNotFoundException 으로 변경 예정
        Like like = Like.builder().member(memberRepository.findById(memberId).orElseThrow(LikeNotFoundException::new))
            .trip(tripService.getTrip(likeRequestDTO.getTripId()))
            .build();
        //여행 entity like_count 증가 로직 추가 예정
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
        Like like = likeRepository.findByMemberIdAndTripId(memberId, tripId);
        if(like==null){
            throw new LikeNotFoundException();
        }else{
            return new LikeResponseDTO(like);
        }
    }
}