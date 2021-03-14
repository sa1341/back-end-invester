package com.kakaopay.investment.service;

import com.kakaopay.investment.domain.item.dto.InvestmentItemDateRes;
import com.kakaopay.investment.domain.item.dto.MyInvestmentItemRes;
import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.entity.QItem;
import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
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

    @Before
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
}
