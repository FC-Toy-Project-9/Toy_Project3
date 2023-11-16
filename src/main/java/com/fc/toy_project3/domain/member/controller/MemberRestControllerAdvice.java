package com.fc.toy_project3.domain.member.controller;

import com.fc.toy_project3.domain.member.exception.ExistingMemberException;
import com.fc.toy_project3.domain.member.exception.InvalidMemberException;
import com.fc.toy_project3.domain.member.exception.MemberNotFoundException;
import com.fc.toy_project3.global.common.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberRestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> invalidMemberException(
            InvalidMemberException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> existingMemberException(
            ExistingMemberException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> memberNotFoundException(
            MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.res(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
