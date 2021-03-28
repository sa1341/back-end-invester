package com.kakaopay.investment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@RequiredArgsConstructor
@EnableJpaAuditing
@SpringBootApplication
public class InvestmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvestmentApplication.class, args);
    }
}
