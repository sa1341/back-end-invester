package com.kakaopay.investment.domain.item.repository;

import com.kakaopay.investment.domain.item.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
}
