package com.kakaopay.investment.domain.item.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
public class InvestmentItemDateReq {

    @NotBlank(message = "투자 공모 모집 날짜를 입력하세요.")
    private String startedAt;
    @NotBlank(message = "투자 공모 모집 마감 날짜를 입력하세요.")
    private String finishedAt;
}
