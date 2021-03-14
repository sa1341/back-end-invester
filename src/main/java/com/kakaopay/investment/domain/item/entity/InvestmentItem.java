package com.kakaopay.investment.domain.item.entity;

import com.kakaopay.investment.domain.BaseTimeEntity;
import com.kakaopay.investment.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class InvestmentItem extends BaseTimeEntity {

    @Id
    @Column(name = "investment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long investing_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Item item;

    @Builder
    public InvestmentItem(Long investing_amount) {
        this.investing_amount = investing_amount;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addItem(Item item) {
        this.item = item;
    }
}
