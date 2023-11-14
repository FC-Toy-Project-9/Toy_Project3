package com.fc.toy_project3.domain.trip.exception;

/**
 * 알맞지 않은 페이징 요청 예외
 */
public class InvalidPagingRequestException extends RuntimeException {

    public InvalidPagingRequestException(String message) {
        super(message);
    }
}
