package com.fc.toy_project3.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDTO {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
}
