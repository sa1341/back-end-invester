package com.kakaopay.investment.service;

import com.kakaopay.investment.domain.item.dto.InvestmentItemDateRes;
import com.kakaopay.investment.domain.item.dto.MyInvestmentItemRes;
import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.entity.QItem;
import com.kakaopay.investment.domain.item.service.ItemService;
import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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

    public void setUp() {
        queryFactory = new JPAQueryFactory(em);
        Member member = queryFactory.selectFrom(QMember.member)
                .where(QMember.member.id.eq(1L))
                .fetchOne();

        Item item = queryFactory.selectFrom(QItem.item)
                .where(QItem.item.id.eq(1L))
                .fetchOne();

        IntStream.range(1, 9)
                .forEach(index -> {
                    InvestmentItem investmentItem = InvestmentItem.builder()
                            .investing_amount(800000L)
                            .build();
                    investmentItem.addItem(item);
                    investmentItem.addMember(member);
                    em.persist(investmentItem);
                });
    }

    @Test
    public void 누적투자금액을_구한다() throws Exception {

        Long result = queryFactory.select(investmentItem.investing_amount.sum().coalesce(0L))
                .from(investmentItem)
                .join(investmentItem.item, item)
                .where(item.id.eq(1L))
                .fetchOne();

        logger.info("sum: " + result);
        Assertions.assertThat(result).isEqualTo(7400000);
    }

    @Test
    public void 나의_투자상품_조회_테스트() throws Exception {

        //given
        List<MyInvestmentItemRes> itemList = queryFactory.select(Projections.constructor(MyInvestmentItemRes.class,
                item.id.as("id"),
                item.title.as("title"),
                item.total_investing_amount.as("totalInvestingAmount"),
                investmentItem.investing_amount.as("myInvestingAmount"),
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
                item.total_investing_amount.as("totalInvestingAmount"),
                investmentItem.investing_amount.sum().coalesce(0L).as("currentInvestingAmount"),
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


    @Test
    @DisplayName("투자금액 줄여보기(멀티스레드) 테스트")
    public void 투자금액_줄이기_테스트() throws Exception {
        //given
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExecute = 100;
        CountDownLatch latch = new CountDownLatch(numberOfExecute);
        ExecutorService service = Executors.newFixedThreadPool(10);

        //when
        for (int i = 0; i < numberOfExecute; i++) {
            service.execute(() -> {
                try {
                    itemService.decreaseTotalAmount("부동산 포트폴리오", 10000L);
                    successCount.getAndIncrement();
                    System.out.println("성공");
                } catch (ObjectOptimisticLockingFailureException oe) {
                    System.out.println("충돌감지");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                latch.countDown();
            });
        }

        latch.await();
        //then
        Assertions.assertThat(successCount.get()).isEqualTo(100);
    }
}
