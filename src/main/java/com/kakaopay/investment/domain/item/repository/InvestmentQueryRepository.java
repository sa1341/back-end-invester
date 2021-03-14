package com.kakaopay.investment.domain.item.repository;

import com.kakaopay.investment.domain.item.dto.InvestmentItemDateRes;
import com.kakaopay.investment.domain.item.dto.MyInvestmentItemRes;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.kakaopay.investment.domain.item.entity.QInvestmentItem.investmentItem;
import static com.kakaopay.investment.domain.item.entity.QItem.item;
import static com.kakaopay.investment.domain.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class InvestmentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Long getAccumulatedAmount(Item item) {
        Long result = queryFactory.select(investmentItem.investing_amount.sum().coalesce(0L))
                .from(investmentItem)
                .join(investmentItem.item, QItem.item)
                .where(QItem.item.id.eq(item.getId()))
                .fetchOne();

        return result;
    }

    public List<MyInvestmentItemRes> getInvestmentItems(Long memberId) {
        List<MyInvestmentItemRes> itemList = queryFactory.select(Projections.constructor(MyInvestmentItemRes.class,
                item.id.as("id"),
                item.title.as("title"),
                item.total_investing_amount.as("totalInvestingAmount"),
                investmentItem.investing_amount.as("myInvestingAmount"),
                investmentItem.startedAt.as("startedAt")))
                .from(investmentItem)
                .join(investmentItem.member, member)
                .join(investmentItem.item, item)
                .where(member.id.eq(memberId))
                .fetch();

        return itemList;
    }

    public List<InvestmentItemDateRes> getInvestmentItemsByDate(LocalDateTime startDateTime, LocalDateTime finishedDateTime) {
        List<InvestmentItemDateRes> result = queryFactory.select(Projections.constructor(InvestmentItemDateRes.class,
                item.id.as("id"),
                item.title.as("title"),
                item.total_investing_amount.as("totalInvestingAmount"),
                investmentItem.investing_amount.sum().coalesce(0L).as("currentInvestingAmount"),
                investmentItem.count().as("investorCount"),
                item.itemStatus.as("investingStatus"),
                item.startedAt,
                item.finishedAt))
                .from(investmentItem)
                .join(investmentItem.item, item)
                .where(item.startedAt.goe(startDateTime).and(item.finishedAt.loe(finishedDateTime)))
                .groupBy(item.id)
                .fetch();
        return result;
    }
}
