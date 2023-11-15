package com.fc.toy_project3.domain.itinerary.exception;

public class NotItineraryAuthorException extends RuntimeException{

    public NotItineraryAuthorException() {
        super("여정 정보 작성자가 아닙니다.");
    }
}
