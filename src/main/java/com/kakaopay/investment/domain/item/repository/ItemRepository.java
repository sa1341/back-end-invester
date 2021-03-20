package com.kakaopay.investment.domain.item.repository;

import com.kakaopay.investment.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByTitle(String title);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.title = :title")
    Item findWithTitleForUpdate(@Param("title") String title);
}
