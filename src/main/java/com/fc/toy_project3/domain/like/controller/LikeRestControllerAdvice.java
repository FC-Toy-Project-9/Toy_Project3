package com.fc.toy_project3.domain.like.controller;

import com.fc.toy_project3.domain.like.dto.response.LikeResponseDTO;
import com.fc.toy_project3.domain.like.exception.LikeNotFoundException;
import com.fc.toy_project3.domain.like.exception.LikeUnauthorizedException;
import com.fc.toy_project3.global.common.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Like REST Controller Advice
 */
@RestControllerAdvice
public class LikeRestControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ResponseDTO<LikeResponseDTO>> likeNotFoundException(LikeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<LikeResponseDTO>> likeUnauthorizedException(
        LikeUnauthorizedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO.res(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }
}
