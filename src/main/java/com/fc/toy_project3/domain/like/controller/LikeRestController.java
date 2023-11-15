package com.fc.toy_project3.domain.like.controller;

import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.global.common.ResponseDTO;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Like REST Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeRestController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ResponseDTO<LikeResponseDTO>> createLike(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody LikeRequestDTO likeRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDTO.res(HttpStatus.CREATED, likeService.createLike(customUserDetails.getMemberId(), likeRequestDTO),
                "성공적으로 좋아요 정보를 등록했습니다."));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<ResponseDTO<LikeResponseDTO>> getLikeByMemberIdAndTripId(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable Long tripId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, likeService.getLikeByMemberIdAndTripId(customUserDetails.getMemberId(), tripId),
                "성공적으로 좋아요 정보를 조회했습니다."));
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<ResponseDTO<LikeResponseDTO>> deleteLikeById(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable Long likeId){
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDTO.res(HttpStatus.OK, likeService.deleteLikeById(customUserDetails.getMemberId(), likeId),
                "성공적으로 좋아요 정보를 삭제했습니다."));
    }
}