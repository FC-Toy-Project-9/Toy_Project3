package com.fc.toy_project3.domain.trip.exception;

/**
 * 여행 정보를 찾을 수 없는 예외
 */
public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException() {
        super("여행 기록을 찾을 수 없습니다.");
    }
}
