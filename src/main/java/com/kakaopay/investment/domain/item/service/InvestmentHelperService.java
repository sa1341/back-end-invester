package com.kakaopay.investment.domain.item.service;

import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.exception.ItemNotFoundException;
import com.kakaopay.investment.domain.item.repository.ItemRepository;

import java.util.Optional;

public final class InvestmentHelperService {

    public static Item findExistingItem(final ItemRepository itemRepository, Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (!optionalItem.isPresent()) {
            throw new ItemNotFoundException("Item is not exist: " + itemId);
        }
        return optionalItem.get();
    }
}
