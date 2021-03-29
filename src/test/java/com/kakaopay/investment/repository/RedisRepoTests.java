package com.kakaopay.investment.repository;


import com.kakaopay.investment.domain.item.entity.InvestmentAmount;
import com.kakaopay.investment.domain.item.repository.InvestmentAmountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisRepoTests {

    @Autowired
    private InvestmentAmountRepository amountRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Before
    public void setUp() {
      InvestmentAmount investmentAmount1 = InvestmentAmount.builder()
                .id(1L)
                .title("개인자산 포트폴리오")
                .accumulatedAmount(1000L)
                .build();

        InvestmentAmount investmentAmount2 = InvestmentAmount.builder()
                .id(1L)
                .title("개인자산 포트폴리오")
                .accumulatedAmount(1000L)
                .build();

        amountRepository.save(investmentAmount1);
        amountRepository.save(investmentAmount2);
    }

    @Test
    public void redis_누적금액_조회_테스트() throws Exception {

        //given
        //when
        Optional<InvestmentAmount> optionalInvestmentAmount = amountRepository.findById(1L);

        if (optionalInvestmentAmount.isPresent()) {
            InvestmentAmount findInvestmentAmount = optionalInvestmentAmount.get();
            System.out.println(findInvestmentAmount.getId());
            System.out.println(findInvestmentAmount.getTitle());
            System.out.println(findInvestmentAmount.getAccumulatedAmount());
           assertThat(findInvestmentAmount.getId()).isEqualTo(1L);
        }
    }

}
