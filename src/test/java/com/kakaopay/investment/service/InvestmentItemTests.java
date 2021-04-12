package com.kakaopay.investment.service;

import com.kakaopay.investment.domain.item.dto.InvestmentItemDateRes;
import com.kakaopay.investment.domain.item.dto.MyInvestmentItemRes;
import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.entity.QItem;
import com.kakaopay.investment.domain.item.service.ItemService;
import com.kakaopay.investment.domain.item.status.ItemStatus;
import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.entity.QMember;
import com.kakaopay.investment.domain.member.service.MemberService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.kakaopay.investment.domain.item.entity.QInvestmentItem.investmentItem;
import static com.kakaopay.investment.domain.item.entity.QItem.item;
import static com.kakaopay.investment.domain.member.entity.QMember.member;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class InvestmentItemTests {

    private final Logger logger = LoggerFactory.getLogger(InvestmentItemTests.class);

    @Autowired
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @Autowired
    private ItemService itemService;

    @Autowired
    private MemberService memberService;

    @Before
    public void setUp() {
        queryFactory = new JPAQueryFactory(em);
        createItem(itemService);
        createMember(memberService);

        Member member = queryFactory.selectFrom(QMember.member)
                .where(QMember.member.id.eq(1L))
                .fetchOne();

        Item item = queryFactory.selectFrom(QItem.item)
                .where(QItem.item.id.eq(1l))
                .fetchOne();

        createInvestmentItems(member, item);
    }

 /*   @Test
    public void 누적투자금액을_구한다() throws Exception {
        List<InvestmentItem> result = queryFactory.select(investmentItem)
                .from(investmentItem)
                .join(investmentItem.item, item)
                .fetchJoin()
                .where(item.id.eq(1L))
                .fetch();

        Long accumulatedAmount = result.stream()
                .mapToLong(InvestmentItem::getInvestingAmount)
                .sum();

        assertThat(accumulatedAmount).isEqualTo(180000);
    }*/

    @Test
    public void 나의_투자상품_조회_테스트() throws Exception {

        //given
        List<MyInvestmentItemRes> itemList = queryFactory.select(Projections.constructor(MyInvestmentItemRes.class,
                item.id.as("id"),
                item.title.as("title"),
                item.totalInvestingAmount.as("totalInvestingAmount"),
                investmentItem.investingAmount.as("myInvestingAmount"),
                investmentItem.startedAt.as("startedAt")
        ))
                .from(investmentItem)
                .join(investmentItem.member, member)
                .join(investmentItem.item, item)
                .where(member.id.eq(1L))
                .fetch();

        //when
        itemList.stream()
                .forEach(myItem -> {
                    logger.info("itemId: {}", myItem.getId());
                    logger.info("title: {}", myItem.getTitle());
                    logger.info("totalInvestingAmount: {}", myItem.getTotalInvestingAmount());
                    logger.info("myInvestingAmount: {}", myItem.getMyInvestingAmount());
                    logger.info("startedAt: ", myItem.getStartedAt());
                });

        //then
    }

    @Test
    public void 전체_투자상품_조회_테스트() throws Exception {

        //given
        String startDate = "2021-03-13 21:30:10";
        String finishedDate = "2021-06-01 23:59:59";

        LocalDateTime startDateTime = getLocalDateTime(startDate);
        LocalDateTime finishedDateTime = getLocalDateTime(finishedDate);

        //when
        List<InvestmentItemDateRes> result = queryFactory.select(Projections.constructor(InvestmentItemDateRes.class,
                item.id.as("id"),
                item.title.as("title"),
                item.totalInvestingAmount.as("totalInvestingAmount"),
                investmentItem.investingAmount.sum().coalesce(0L).as("currentInvestingAmount"),
                investmentItem.count().as("investorCount"),
                item.itemStatus.as("investingStatus"),
                item.startedAt,
                item.finishedAt
        ))
                .from(investmentItem)
                .join(investmentItem.item, item)
                .where(item.startedAt.goe(startDateTime).and(item.finishedAt.loe(finishedDateTime)))
                .groupBy(item.id)
                .fetch();

        //then
        result.forEach(item -> {
            logger.info(item.toString());
        });

    }

    private LocalDateTime getLocalDateTime(String currentDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentDate, dateTimeFormatter);
        return localDateTime;
    }



    private static void createMember(MemberService memberService) {
        Member member1 = Member.builder()
                .name("임준영")
                .email("a79007714@gmail.com")
                .build();

        Member member2 = Member.builder()
                .name("김광용")
                .email("ahffkdbfkrql@naver.com")
                .build();
        memberService.join(member1);
        memberService.join(member2);
    }

    private void createInvestmentItems(Member member, Item item) {
        InvestmentItem investmentItem1 = InvestmentItem.builder()
                .investingAmount(100000L)
                .build();
        investmentItem1.addMember(member);
        investmentItem1.addItem(item);

        InvestmentItem investmentItem2 = InvestmentItem.builder()
                .investingAmount(80000L)
                .build();
        investmentItem2.addMember(member);
        investmentItem2.addItem(item);

        em.persist(investmentItem1);
        em.persist(investmentItem2);
    }

    private static void createItem(ItemService itemService) {
        Item item1 = Item.builder()
                .title("개인신용 포트폴리오")
                .totalInvestingAmount(1000000L)
                .finishedAt("2021-04-01 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        Item item2 = Item.builder()
                .title("부동산 포트폴리오")
                .totalInvestingAmount(5000000L)
                .finishedAt("2021-05-31 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        Item item3 = Item.builder()
                .title("주식 포트폴리오")
                .totalInvestingAmount(3000000L)
                .finishedAt("2021-03-15 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        Item item4 = Item.builder()
                .title("펀드 포트폴리오")
                .totalInvestingAmount(9000000L)
                .finishedAt("2021-10-31 23:59:59")
                .itemStatus(ItemStatus.IN_PROGRESS)
                .build();

        itemService.saveItem(item1);
        itemService.saveItem(item2);
        itemService.saveItem(item3);
        itemService.saveItem(item4);
    }
}
