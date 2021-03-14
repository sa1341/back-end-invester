package com.kakaopay.investment.domain.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class InvestmentItemReq {

    @NotNull(message = "상품 id는 필수입니다.")
    private Long itemId;
    @NotNull(message = "투자 금액은 필수입니다.")
    private Long investingAmount;
}
