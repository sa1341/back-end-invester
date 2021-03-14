package com.kakaopay.investment.domain.item.entity;

import com.kakaopay.investment.domain.BaseTimeEntity;
import com.kakaopay.investment.domain.item.status.ItemStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseTimeEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long total_investing_amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ItemStatus itemStatus;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Builder
    public Item(String title, Long total_investing_amount, String finishedAt, ItemStatus itemStatus) {
        this.title = title;
        this.total_investing_amount = total_investing_amount;
        this.finishedAt = convertLocalDateTime(finishedAt);
        this.itemStatus = itemStatus;
    }

    private LocalDateTime convertLocalDateTime(String finishedAt) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(finishedAt, dateTimeFormatter);
    }

    // 총 투자 모금 금액과 누적 금액을 뺀 나머지 투자금액이랑 회원이 투자하려는 금액을 비교하여 InvestmentItem 엔티티의 생성 유무를 결정 함.
    public InvestmentItem createInvestmentItem(Long investingAmount, Long accumulatedAmount) {
        Long remainingAmount = this.total_investing_amount - accumulatedAmount;

        if (remainingAmount >= investingAmount) {
            InvestmentItem investmentItem = InvestmentItem.builder()
                    .investing_amount(investingAmount)
                    .build();
            investmentItem.addItem(this);
            if (remainingAmount.equals(investingAmount)) {
                changeItemStatus(ItemStatus.COMPLETED);
            }
            return investmentItem;
        }
        return null;
    }

    private void changeItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }
}
