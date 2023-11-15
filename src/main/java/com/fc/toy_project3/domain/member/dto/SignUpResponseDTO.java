package com.fc.toy_project3.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponseDTO {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;

    @Builder
    public SignUpResponseDTO (Long memberId, String email, String name, String nickname) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }

}
