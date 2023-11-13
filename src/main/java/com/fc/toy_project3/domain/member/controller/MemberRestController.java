package com.fc.toy_project3.domain.member.controller;

import com.fc.toy_project3.domain.member.dto.*;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.global.common.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDTO<MemberResponseDTO>> signUp (
            @Valid @RequestBody SignUpRequestDTO signUpRequestDTO
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.res(HttpStatus.CREATED,
                memberService.signUp(signUpRequestDTO),
                "회원가입이 완료되었습니다."));
    }

}
