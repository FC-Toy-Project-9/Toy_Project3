package com.fc.toy_project3.domain.like.controller;

import com.fc.toy_project3.domain.like.dto.request.LikeRequestDTO;
import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.service.LikeService;
import com.fc.toy_project3.global.common.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO<LikeResponseDTO>> createLike(@Valid @RequestBody LikeRequestDTO likeRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDTO.res(HttpStatus.CREATED, likeService.createLike(1L, likeRequestDTO),
                "성공적으로 좋아요 정보를 등록했습니다."));
    }
}