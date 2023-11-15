package com.fc.toy_project3.domain.trip.exception;

public class NotTripAuthorException extends RuntimeException{

    public NotTripAuthorException() {
        super("여행 정보 작성자가 아닙니다.");
    }
}
