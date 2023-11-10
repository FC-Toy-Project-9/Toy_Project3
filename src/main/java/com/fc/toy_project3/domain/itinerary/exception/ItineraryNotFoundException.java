package com.fc.toy_project3.domain.itinerary.exception;

/**
 * 여정 정보를 찾을 수 없는 예외
 */
public class ItineraryNotFoundException extends RuntimeException {

    public ItineraryNotFoundException() {
        super("여정 기록을 찾을 수 없습니다.");
    }

}

