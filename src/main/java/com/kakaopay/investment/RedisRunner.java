package com.kakaopay.investment;

import com.kakaopay.investment.domain.item.repository.InvestmentAmountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisRunner implements ApplicationRunner {

    private final StringRedisTemplate redisTemplate;
    private final InvestmentAmountRepository investmentAmountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set("junyoung", "syn1341");
        value.set("minwan", "minwan1");
        value.set("dog", "marry");
    }
}
