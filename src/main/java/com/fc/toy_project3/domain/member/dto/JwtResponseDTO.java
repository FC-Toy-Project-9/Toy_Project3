package com.fc.toy_project3.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponseDTO {
    private String jwtToken;

}
