package com.kakaopay.investment.domain.item.repository;

import com.kakaopay.investment.domain.item.entity.InvestmentAmount;
import org.springframework.data.repository.CrudRepository;

public interface InvestmentAmountRepository extends CrudRepository<InvestmentAmount, Long> {

}
