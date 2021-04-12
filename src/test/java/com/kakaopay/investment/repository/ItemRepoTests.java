package com.kakaopay.investment.repository;

import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.repository.ItemRepository;
import com.kakaopay.investment.domain.item.status.ItemStatus;
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
                        .title("개인신용 포트폴리오")
                        .totalInvestingAmount(1000000L)
                        .itemStatus(ItemStatus.IN_PROGRESS)
                        .finishedAt("2021-09-30 23:59:59")
                        .build();

        itemRepository.save(item1);
    }

    @Test
    public void item_조회_테스트() throws Exception {
        //given
        Optional<Item> optionalItem1 = itemRepository.findById(1L);
        //when
        Item item1 = optionalItem1.orElse(null);
        //then
        Assertions.assertThat(item1.getTitle()).isEqualTo("개인신용 포트폴리오");
     }
}
