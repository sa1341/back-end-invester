package com.kakaopay.investment.domain.item.service;

import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(Item item) {
        itemRepository.save(item);
    }
}
