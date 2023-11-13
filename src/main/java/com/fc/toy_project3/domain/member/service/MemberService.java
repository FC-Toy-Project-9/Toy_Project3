package com.fc.toy_project3.domain.member.service;

import com.fc.toy_project3.domain.member.dto.MemberResponseDTO;
import com.fc.toy_project3.domain.member.dto.SignUpRequestDTO;
import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.exception.ExistingMemberException;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import com.fc.toy_project3.global.config.PasswordEncoderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public MemberResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        // 데이터베이스에서 확인
        String email = signUpRequestDTO.getEmail();
        String nickname = signUpRequestDTO.getNickname();

        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new ExistingMemberException("이미 존재하는 회원입니다.");
        });

        memberRepository.findByNickname(nickname).ifPresent(member -> {
            throw new ExistingMemberException("이미 존재하는 닉네임입니다.");
        });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());

        Member member = Member.builder()
                .email(email)
                .name(signUpRequestDTO.getName())
                .nickname(nickname)
                .password(encodedPassword)
                .build();

        return memberRepository.save(member).toMemberResponseDTO();
    }

}
