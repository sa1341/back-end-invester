package com.kakaopay.investment.domain.item.dto;

import com.kakaopay.investment.domain.item.status.ItemStatus;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvestmentItemDateRes {

    private Long id;
    private String title;
    private Long totalInvestingAmount;
    private Long currentInvestingAmount;
    private Long investorCount;
    private ItemStatus investingStatus;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
