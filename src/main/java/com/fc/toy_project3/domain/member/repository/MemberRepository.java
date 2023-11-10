package com.fc.toy_project3.domain.member.repository;

import com.fc.toy_project3.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
