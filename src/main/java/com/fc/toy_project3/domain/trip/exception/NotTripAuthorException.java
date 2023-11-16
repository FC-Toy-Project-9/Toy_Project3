package com.fc.toy_project3.domain.trip.exception;

/**
 * 여행 작성자가 아닌 회원의 생성 수정 혹은 요청 익셉션
 */
public class NotTripAuthorException extends RuntimeException{

    public NotTripAuthorException() {
        super("여행 정보 작성자가 아닙니다.");
    }
}
