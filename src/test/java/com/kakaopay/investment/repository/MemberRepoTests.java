package com.kakaopay.investment.repository;

import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.exception.MemberNotFoundException;
import com.kakaopay.investment.domain.member.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepoTests {

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setUp() {
        Member member1 = Member.builder()
                .name("임준영")
                .email("a79007714@gmail.com")
                .build();
        memberRepository.save(member1);
    }


  @Test()
    public void 회원정보_조회_테스트() throws Exception {
        //given
        Long memberId1 = 1L;
        //when
        Optional<Member> optionalMember1 = memberRepository.findById(memberId1);

        //then
        Member findMember1 = optionalMember1.orElseThrow(() -> new MemberNotFoundException());
        assertThat(findMember1.getEmail()).isEqualTo("a79007714@gmail.com");
    }
}
