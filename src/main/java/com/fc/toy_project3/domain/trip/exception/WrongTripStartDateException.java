package com.fc.toy_project3.domain.trip.exception;

/**
 * 알맞지 않은 시작일 예외
 */
public class WrongTripStartDateException extends RuntimeException {

    public WrongTripStartDateException() {
        super("여행 시작일을 다시 확인해주세요.");
    }
}
