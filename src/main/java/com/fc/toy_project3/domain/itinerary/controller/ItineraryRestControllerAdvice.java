package com.fc.toy_project3.domain.itinerary.controller;

import com.fc.toy_project3.domain.itinerary.exception.InvalidItineraryException;
import com.fc.toy_project3.domain.itinerary.exception.ItineraryNotFoundException;
import com.fc.toy_project3.global.common.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ItineraryRestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> itineraryNotFoundException(
        ItineraryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> invalidItineraryException(
        InvalidItineraryException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
