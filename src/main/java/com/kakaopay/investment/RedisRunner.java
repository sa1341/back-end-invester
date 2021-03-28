package com.kakaopay.investment;

import com.kakaopay.investment.domain.item.entity.Account;
import com.kakaopay.investment.domain.item.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RedisRunner implements ApplicationRunner {

    private final StringRedisTemplate redisTemplate;
    private final AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set("junyoung", "syn1341");
        value.set("minwan", "minwan1");
        value.set("dog", "marry");


        Account account = Account.builder()
                                .id("sa1341")
                                .email("a79007714@gmail.com")
                                .username("임준영")
                                .build();

        accountRepository.save(account);

        Optional<Account> findAccount = accountRepository.findById("sa1341");
        if (findAccount.isPresent())
            System.out.println(findAccount.get().getId());
            System.out.println(findAccount.get().getEmail());
            System.out.println(findAccount.get().getUsername());
    }
}
