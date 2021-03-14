package com.kakaopay.investment.domain.item.repository;

import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentItemRepository extends JpaRepository<InvestmentItem, Long> {
}
