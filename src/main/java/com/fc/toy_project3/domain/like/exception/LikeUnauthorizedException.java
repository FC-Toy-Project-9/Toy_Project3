package com.fc.toy_project3.domain.like.exception;

public class LikeUnauthorizedException extends RuntimeException {
    public LikeUnauthorizedException() {
        super("해당 좋아요 정보를 등록한 회원이 아닙니다.");
    }
}
