package com.kakaopay.investment.domain.member.dto;

import com.kakaopay.investment.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class NewMemberReq {
    private String id;
    private String name;
    private String email;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .build();
    }
}
