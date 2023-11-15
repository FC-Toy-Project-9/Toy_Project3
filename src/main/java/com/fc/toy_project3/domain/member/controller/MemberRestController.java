package com.fc.toy_project3.domain.member.controller;

import com.fc.toy_project3.domain.member.dto.JwtResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignInRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.domain.member.service.MemberService;
import com.fc.toy_project3.domain.trip.dto.request.PostTripRequestDTO;
import com.fc.toy_project3.domain.trip.dto.response.TripResponseDTO;
import com.fc.toy_project3.domain.trip.service.TripService;
import com.fc.toy_project3.global.common.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.fc.toy_project3.global.config.jwt.CustomUserDetails;

/**
 * Member REST Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;
    private final TripService tripService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<SignUpResponseDTO>> signUp (
            @Valid @RequestBody SignUpRequestDTO signUpRequestDTO ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.res(HttpStatus.CREATED,
                memberService.signUp(signUpRequestDTO),
                "회원가입이 완료되었습니다."));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseDTO<JwtResponseDTO>> signUp (
            @Valid @RequestBody SignInRequestDTO signInRequestDTO ) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.res(HttpStatus.OK,
                memberService.signIn(signInRequestDTO),
                "로그인이 완료되었습니다."));
    }

    @PostMapping("/test")
    public ResponseEntity<ResponseDTO<SignUpResponseDTO>> test () {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.res(HttpStatus.OK,
                memberService.userInfo(),
                "성공"));
    }

    @PostMapping("/testTrip")
    public ResponseEntity<ResponseDTO<TripResponseDTO>> postTrip
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @Valid @RequestBody PostTripRequestDTO postTripRequestDTO) {
        Long memberId = customUserDetails.getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.res(HttpStatus.OK,
                tripService.postTrip(postTripRequestDTO, memberId),
                "성공"));
    }

}
