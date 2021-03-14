package com.kakaopay.investment.domain.item.service;

import com.kakaopay.investment.domain.item.dto.*;
import com.kakaopay.investment.domain.item.entity.InvestmentItem;
import com.kakaopay.investment.domain.item.entity.Item;
import com.kakaopay.investment.domain.item.repository.InvestmentItemRepository;
import com.kakaopay.investment.domain.item.repository.InvestmentQueryRepository;
import com.kakaopay.investment.domain.item.repository.ItemRepository;
import com.kakaopay.investment.domain.item.status.InvestmentStatus;
import com.kakaopay.investment.domain.member.entity.Member;
import com.kakaopay.investment.domain.member.repository.MemberRepository;
import com.kakaopay.investment.util.LocalDateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.kakaopay.investment.domain.item.service.InvestmentHelperService.findExistingItem;
import static com.kakaopay.investment.domain.member.service.MemberHelperService.findExistingMember;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvestItemService {

    private final InvestmentItemRepository investmentItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final InvestmentQueryRepository investmentQueryRepository;

    /**
     * 전체 투자 상품 조회 API
     * @param investmentItemDateReq (상품 모집 기간(started_at, finished_at))
     * @return
     */

    @Transactional
    public Result<List<InvestmentItemDateRes>> fetchInvestmentItemsByDate(InvestmentItemDateReq investmentItemDateReq) {
        LocalDateTime startDateTime = LocalDateParser.getLocalDateTime(investmentItemDateReq.getStartedAt());
        LocalDateTime finishedDateTime = LocalDateParser.getLocalDateTime(investmentItemDateReq.getFinishedAt());
        List<InvestmentItemDateRes> investmentItemDateRes = investmentQueryRepository.getInvestmentItemsByDate(startDateTime, finishedDateTime);
        Result<List<InvestmentItemDateRes>> result = new Result<>(investmentItemDateRes.size(), investmentItemDateRes);
        return result;
    }

    /**
     * 투자하기 API
     * @param memberId (사용자 식별 값)
     * @param investmentItemReq (상품 ID, 투자 금액이 포함된 DTO 객체)
     * @return
     */
    @Transactional
    public InvestmentItemRes investItem(final String memberId, final InvestmentItemReq investmentItemReq) {
        // 회원, 상품 엔티티 식별 값으로 리포지토리로 조회하여  검증 함.
        Member member = findExistingMember(memberRepository, Long.valueOf(memberId));
        Item item = findExistingItem(itemRepository, investmentItemReq.getItemId());
        // 사용자가 현재 투자할 금액 조회
        Long investingAmount = investmentItemReq.getInvestingAmount();
        // 누적 투자 금액 조회 메서드
        Long accumulatedAmount = investmentQueryRepository.getAccumulatedAmount(item);
        log.info("investingAmount: {}, accumulatedAmount: {}", investingAmount, accumulatedAmount);
        InvestmentItem investmentItem = item.createInvestmentItem(investingAmount, accumulatedAmount);
        String result = saveInvestmentItem(member, investmentItem);
        InvestmentItemRes investmentItemRes = InvestmentItemRes.create(result);
        return investmentItemRes;
    }

    private String saveInvestmentItem(Member member, InvestmentItem investmentItem) {
        if (investmentItem == null) {
            return InvestmentStatus.SOLD_OUT.getStatus();
        }
        investmentItem.addMember(member);
        investmentItemRepository.save(investmentItem);
        return InvestmentStatus.SUCCESS.getStatus();
    }


    /**
     * 나의 투자 상품 조회 API
     * @param memberId (회원 식별 값)
     * @return
     */
    @Transactional
    public Result<List<MyInvestmentItemRes>> fetchInvestmentItems(String memberId) {
        Member member = findExistingMember(memberRepository, Long.valueOf(memberId));
        List<MyInvestmentItemRes> investmentItems = investmentQueryRepository.getInvestmentItems(member.getId());
        Result<List<MyInvestmentItemRes>> result = new Result<>(investmentItems.size(), investmentItems);
        return result;
    }
}
