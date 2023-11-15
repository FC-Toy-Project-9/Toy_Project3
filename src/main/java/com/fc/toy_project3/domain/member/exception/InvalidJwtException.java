package com.fc.toy_project3.domain.member.exception;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message){
        super(message);
    }
}
