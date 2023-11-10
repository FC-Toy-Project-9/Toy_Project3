package com.fc.toy_project3.domain.itinerary.exception;

/**
 * 여정의 이동 정보가 유효하지 않을 때 발생하는 예외
 */
public class InvalidItineraryException extends RuntimeException {

    public InvalidItineraryException(String message) {
        super(message);
    }
}
