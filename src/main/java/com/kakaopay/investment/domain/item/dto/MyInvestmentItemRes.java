package com.kakaopay.investment.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyInvestmentItemRes {

    private Long id;
    private String title;
    private Long totalInvestingAmount;
    private Long myInvestingAmount;
    private LocalDateTime startedAt;
}
