package com.kakaopay.investment.repository;

import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.status.ItemStatus;
import com.kakaopay.investment.domain.member.entity.Member;
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

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class InvestmentItemRepoTests {

    private final Logger logger = LoggerFactory.getLogger(InvestmentItemRepoTests.class);

    @Autowired
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        //given
        Member member = Member.builder()
                              .name("임준영")
                              .email("a79007714@gmail.com")
                              .build();
        Item item = Item.builder()
                        .title("개인신용 포트폴리오")
                        .total_investing_amount(1000000L)
                        .itemStatus(ItemStatus.IN_PROGRESS)
                        .build();

        InvestmentItem investmentItem = InvestmentItem.builder()
                                                      .investing_amount(100000L)
                                                      .build();

        investmentItem.addItem(item);
        investmentItem.addMember(member);

        //when
        //then
         em.persist(member);
         em.persist(item);
         em.persist(investmentItem);

        logger.info("Persistence Context End!!!");
     }

     @Test
     public void investmentItem_조회_테스트() throws Exception {
         //given
         InvestmentItem investmentItem = em.find(InvestmentItem.class, 1L);

         //when
         logger.info("investingAmount: {}",investmentItem.getInvesting_amount());
         logger.info("itemTitle: {}", investmentItem.getItem().getTitle());
         logger.info("email: {}", investmentItem.getMember().getEmail());

         //then
         Assertions.assertThat(investmentItem.getInvesting_amount()).isEqualTo(100000L);
         Assertions.assertThat(investmentItem.getItem().getTitle()).isEqualTo("개인신용 포트폴리오");
         Assertions.assertThat(investmentItem.getMember().getEmail()).isEqualTo("a79007714@gmail.com");
      }
}
