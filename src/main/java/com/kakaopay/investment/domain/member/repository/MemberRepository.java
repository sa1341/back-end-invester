package com.kakaopay.investment.domain.member.repository;

import com.kakaopay.investment.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
