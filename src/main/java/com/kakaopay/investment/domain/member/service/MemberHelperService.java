package com.kakaopay.investment.domain.member.service;

import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.exception.MemberNotFoundException;
import com.kakaopay.investment.domain.member.repository.MemberRepository;

import java.util.Optional;

public final class MemberHelperService {
    public static Member findExistingMember(MemberRepository repo, Long memberId) {
        Optional<Member> findMember = repo.findById(memberId);
        if (!findMember.isPresent()) {
            throw new MemberNotFoundException("Member ID is not exist: " + memberId);
        }
        return findMember.get();
    }
}
