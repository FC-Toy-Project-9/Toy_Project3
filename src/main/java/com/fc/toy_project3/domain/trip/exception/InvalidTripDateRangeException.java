package com.fc.toy_project3.domain.trip.exception;

/**
 * 알맞지 않은 여행 날짜 범위 (시작일>종료일) 예외
 */
public class InvalidTripDateRangeException extends RuntimeException {

    public InvalidTripDateRangeException(){ super("시작일이 종료일보다 빨라야 합니다."); }

}
