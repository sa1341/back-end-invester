package com.kakaopay.investment.repository;

import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.exception.ItemNotFoundException;
import com.kakaopay.investment.domain.item.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepoTests {

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        Item item1 = Item.builder()
                        .title("개인 신용 포트폴리오")
                        .total_investing_amount(1000000L)
                        .build();

        Item item2 = Item.builder()
                .title("부동산 포트폴리오")
                .total_investing_amount(5000000L)
                .build();

        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test(expected = ItemNotFoundException.class)
    public void item_조회_테스트() throws Exception {
        //given
        Optional<Item> optionalItem1 = itemRepository.findById(3L);
        Optional<Item> optionalItem2 = itemRepository.findById(2L);

        //when
        Item item1 = optionalItem1.orElse(null);
        Item item2 = optionalItem2.orElse(null);

        if (item1 == null || item2 == null) {
            throw new ItemNotFoundException();
        }

        //then
        Assertions.assertThat(item1.getTitle()).isEqualTo("개인 신용 포트폴리오");
        Assertions.assertThat(item2.getTitle()).isEqualTo("부동산 포트폴리오");
     }

}
