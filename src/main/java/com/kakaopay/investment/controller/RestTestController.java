package com.kakaopay.investment.controller;

import com.kakaopay.investment.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestTestController {

    private final ItemService itemService;

    @GetMapping("/api/decrease")
    public String decreaseTotalAmount(@RequestParam(value = "title") String title, @RequestParam(name = "amount") Long amount) {
        log.info("title: {}, amount: {}", title, amount);
        itemService.decreaseTotalAmount(title, amount);
        String result = "현재 총 투자금액" + itemService.currentTotalAmount(title);
        return result;
    }
}
