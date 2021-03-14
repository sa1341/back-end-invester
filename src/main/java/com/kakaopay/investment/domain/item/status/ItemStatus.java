package com.kakaopay.investment.domain.item.status;

public enum ItemStatus {
    IN_PROGRESS("모집중"), COMPLETED("모집완료");

    private String status;

    ItemStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
