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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getInvestingAmount() {
        return investingAmount;
    }

    public void setInvestingAmount(Long investingAmount) {
        this.investingAmount = investingAmount;
    }
}
