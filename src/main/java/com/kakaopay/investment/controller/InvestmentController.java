package com.kakaopay.investment.controller;

import com.kakaopay.investment.domain.item.dto.*;
import com.kakaopay.investment.domain.item.service.InvestItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InvestmentController {

    private final InvestItemService investItemService;

    @GetMapping(value = "/api/investments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Result> fetchInvestmentItemsByDate(@Valid @RequestBody final InvestmentItemDateReq investmentItemDateReq) {
        log.info("InvestmentItemDateReq: {}", investmentItemDateReq);
        Result<List<InvestmentItemDateRes>> result = investItemService.fetchInvestmentItemsByDate(investmentItemDateReq);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/investment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<InvestmentItemRes> investItem(@Valid @RequestBody final InvestmentItemReq investmentItemReq, HttpServletRequest request) {
        String memberId = request.getHeader("X-USER-ID");
        log.info("memberId: {}", memberId);
        log.info("investItemReq: {}", investmentItemReq);
        InvestmentItemRes investmentItemRes = investItemService.investItem(memberId, investmentItemReq);
        return new ResponseEntity<>(investmentItemRes, HttpStatus.OK);
    }

    @GetMapping(value = "/api/investments/investment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Result> fetchInvestmentItems(@RequestHeader(name = "X-USER-ID") final String memberId) {
        log.info("memberId: {}", memberId);
        Result<List<MyInvestmentItemRes>> result = investItemService.fetchInvestmentItems(memberId);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
