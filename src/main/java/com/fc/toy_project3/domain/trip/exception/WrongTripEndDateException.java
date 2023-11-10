package com.fc.toy_project3.domain.trip.exception;

/**
 * 알맞지 않은 종료일 예외
 */
public class WrongTripEndDateException extends RuntimeException {

    public WrongTripEndDateException() {
        super("여행 종료일을 다시 확인해주세요.");
    }
}
