package com.fc.toy_project3.domain.member.entity;

import com.fc.toy_project3.domain.member.dto.SignUpResponseDTO;
import com.fc.toy_project3.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 30)
    private String email;
    @Column()
    private String password;
    @Column(length = 30)
    private String name;
    @Column(unique = true, length = 30)
    private String nickname;

    @Builder
    public Member(Long id, String email, String password, String name, String nickname) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }
}