package com.kakaopay.investment;

import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.repository.ItemRepository;
import com.kakaopay.investment.domain.item.status.ItemStatus;
import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@RequiredArgsConstructor
@EnableJpaAuditing
@SpringBootApplication
public class InvestmentApplication implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public static void main(String[] args) {
        SpringApplication.run(InvestmentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Member member = Member.builder()
                .name("임준영")
                .email("a79007714@gmail.com")
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .title("개인신용 포트폴리오")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .totalInvestingAmount(1000000L)
                .finishedAt("2021-09-30 23:59:59")
                .build();
        itemRepository.save(item);
    }
}
