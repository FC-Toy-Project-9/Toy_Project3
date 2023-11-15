package com.fc.toy_project3.domain.like.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException() {
        super("좋아요 정보를 찾을 수 없습니다.");
    }
}