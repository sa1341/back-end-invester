package com.kakaopay.investment.domain.item.dto;

import lombok.*;

@Getter
@Setter
public class InvestmentItemRes {

    private final String result;

    protected InvestmentItemRes(String result) {
        this.result = result;
    }

    public static InvestmentItemRes create(String result) {
        return new InvestmentItemRes(result);
    }
}
