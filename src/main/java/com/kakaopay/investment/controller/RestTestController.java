package com.kakaopay.investment.controller;

import com.kakaopay.investment.domain.item.dto.InvestmentItemReq;
import com.kakaopay.investment.domain.item.dto.InvestmentItemRes;
import com.kakaopay.investment.domain.item.service.InvestItemService;
import com.kakaopay.investment.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestTestController {

    private final ItemService itemService;
    private final InvestItemService investItemService;

    @GetMapping("/api/decrease")
    public String decreaseTotalAmount(@RequestParam(value = "title") String title, @RequestParam(name = "amount") Long amount) {
        log.info("title: {}, amount: {}", title, amount);
        itemService.decreaseTotalAmount(title, amount);
        String result = "현재 총 투자금액" + itemService.currentTotalAmount(title);
        return result;
    }

    @GetMapping(value = "/api/test/investment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InvestmentItemRes> doInvestItemTest(@RequestParam(value = "memberId") String memberId,
                                                              @RequestParam(value = "itemId") Long itemId,
                                                              @RequestParam(value = "investingAmount") Long investingAmount)
    {
        InvestmentItemReq investmentItemReq = new InvestmentItemReq();
        investmentItemReq.setItemId(itemId);
        investmentItemReq.setInvestingAmount(investingAmount);
        log.info("memberId: {}, itemId: {}, investingAmount: {}", memberId, itemId, investingAmount);

        InvestmentItemRes investmentItemRes = investItemService.investItem(memberId, investmentItemReq);
        return new ResponseEntity<>(investmentItemRes, HttpStatus.OK);
    }
}
