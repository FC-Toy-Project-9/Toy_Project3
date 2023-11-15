package com.fc.toy_project3.global.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class HandleJwtException {

    @ExceptionHandler
    private void handleJwtException(String message, Exception e) {
        // 예외 처리를 여기에 추가
        throw new JwtException(message, e);
    }
}
