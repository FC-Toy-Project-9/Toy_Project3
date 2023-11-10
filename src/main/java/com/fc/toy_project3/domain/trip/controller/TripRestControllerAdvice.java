package com.fc.toy_project3.domain.trip.controller;

import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.exception.InvalidTripDateRangeException;
import com.fc.toy_project3.domain.trip.exception.TripNotFoundException;
import com.fc.toy_project3.domain.trip.exception.WrongTripEndDateException;
import com.fc.toy_project3.domain.trip.exception.WrongTripStartDateException;
import com.fc.toy_project3.global.common.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Trip REST Controller Advice
 */
@RestControllerAdvice
public class TripRestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<TripResponseDTO>> invalidTripDateRangeException(
        InvalidTripDateRangeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<TripResponseDTO>> tripNotFoundException(
        TripNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<TripResponseDTO>> wrongTripStartDateException(
        WrongTripStartDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<TripResponseDTO>> wrongTripEndDateException(
        WrongTripEndDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
