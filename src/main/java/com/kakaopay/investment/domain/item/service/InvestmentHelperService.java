package com.kakaopay.investment.domain.item.service;

import com.kakaopay.investment.domain.item.entity.InvestmentAmount;
import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.exception.ItemNotFoundException;
import com.kakaopay.investment.domain.item.repository.InvestmentAmountRepository;
import com.kakaopay.investment.domain.item.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

public final class InvestmentHelperService {

    public static Item findExistingItem(final ItemRepository itemRepository, Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (!optionalItem.isPresent()) {
            throw new ItemNotFoundException("Item is not exist: " + itemId);
        }
        return optionalItem.get();
    }

    public static InvestmentAmount findInvestmentAmount(final InvestmentAmountRepository investmentAmountRepository, Item item) {
        Long itemId = item.getId();
        Optional<InvestmentAmount> optionalInvestmentAmount = investmentAmountRepository.findById(itemId);

        if (!optionalInvestmentAmount.isPresent()) {
            return InvestmentAmount.builder()
                    .id(itemId)
                    .accumulatedAmount(0L)
                    .title(item.getTitle())
                    .build();
        }
        return optionalInvestmentAmount.get();
    }

    public static Long getAccumulatedAmount(List<InvestmentItem> investmentItems) {
        Long sum = investmentItems.stream()
                .mapToLong(InvestmentItem::getInvestingAmount)
                .sum();
        return sum;
    }
}
