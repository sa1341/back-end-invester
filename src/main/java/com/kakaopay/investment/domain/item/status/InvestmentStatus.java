package com.kakaopay.investment.domain.item.status;

public enum  InvestmentStatus {
    SUCCESS("success"), SOLD_OUT("sold-out");

    private String status;

    InvestmentStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
