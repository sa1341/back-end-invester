package com.kakaopay.investment.domain.item.service;

import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final EntityManager em;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Long decreaseTotalAmount(String title, Long amount) {
        Item item = itemRepository.findWithTitleForUpdate(title);
        item.decreaseTotalAmount(amount);
        return item.getTotal_investing_amount();
    }


    @Transactional
    public Long currentTotalAmount(String title) {
        Item item = itemRepository.findByTitle(title);
        return item.getTotal_investing_amount();
    }
}
