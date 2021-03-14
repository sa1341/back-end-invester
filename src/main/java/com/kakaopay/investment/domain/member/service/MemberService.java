package com.kakaopay.investment.domain.member.service;

import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void join(final Member member) {
        memberRepository.save(member);
    }

}
