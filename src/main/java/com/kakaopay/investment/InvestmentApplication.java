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
        log.info("CommandLineRunner Start!!!");
        //createMember(memberRepository);
        //createItem(itemRepository);
    }

    private static void createItem(ItemRepository itemRepository) {
        Item item1 = Item.builder()
                .title("개인신용 포트폴리오")
                .total_investing_amount(1000000L)
                .finishedAt("2021-04-01 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        Item item2 = Item.builder()
                .title("부동산 포트폴리오")
                .total_investing_amount(5000000L)
                .finishedAt("2021-05-31 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        Item item3 = Item.builder()
                .title("주식 포트폴리오")
                .total_investing_amount(3000000L)
                .finishedAt("2021-03-15 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        Item item4 = Item.builder()
                .title("펀드 포트폴리오")
                .total_investing_amount(9000000L)
                .finishedAt("2021-10-31 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
    }

    private static void createMember(MemberRepository memberRepository) {

        Member member1 = Member.builder()
                .name("임준영")
                .email("a79007714@gmail.com")
                .build();

        Member member2 = Member.builder()
                .name("김광용")
                .email("ahffkdbfkrql@naver.com")
                .build();

        Member member3 = Member.builder()
                .name("참킴")
                .email("syn7714@gmail.com")
                .build();

        Member member4 = Member.builder()
                .name("임성희")
                .email("sa1341@naver.com")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }

}
