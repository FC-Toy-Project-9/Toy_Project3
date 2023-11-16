package com.fc.toy_project3.domain.member.unit.repository;

import com.fc.toy_project3.domain.member.entity.Member;
import com.fc.toy_project3.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("특정 memberId 값을 가진 Member 객체 조회")
    @Rollback(false) // Rollback을 false로 설정하여 저장한 데이터를 실제로 커밋합니다.
    void testFindById() {
        // given
        Member memberToSave = Member.builder()
                .email("test@example111.com")
                .password("password")
                .name("Test User")
                .nickname("testUser111")
                .build();

        // when
        Member savedMember = memberRepository.save(memberToSave);
        Long memberId = savedMember.getId();

        Optional<Member> foundMemberOptional = memberRepository.findById(memberId);

        // then
        assertNotNull(foundMemberOptional);
        assertEquals(memberToSave.getEmail(), foundMemberOptional.get().getEmail());
        // 추가적인 검증을 수행할 수 있습니다.
    }

    @Test
    @DisplayName("Member 객체 저장")
    void testSaveMember() {
        // given
        Member memberToSave = Member.builder()
                .email("test@example1111.com")
                .password("password")
                .name("Test User1111")
                .nickname("testUser")
                .build();

        Optional<Member> existingMember = memberRepository.findByNickname(memberToSave.getNickname());
        if (existingMember.isPresent()) {
            // 중복된 닉네임 회원이 존재하면 처리할 로직 추가
            throw new RuntimeException("중복된 닉네임 회원이 존재합니다.");
        }

        // when
        Member savedMember = memberRepository.save(memberToSave);

        // then
        assertNotNull(savedMember.getId());
        assertEquals(memberToSave.getEmail(), savedMember.getEmail());
        // 추가적인 검증을 수행할 수 있습니다.
    }
}