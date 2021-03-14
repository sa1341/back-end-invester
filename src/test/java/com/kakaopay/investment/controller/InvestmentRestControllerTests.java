package com.kakaopay.investment.controller;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@WebMvcTest
public class InvestmentRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 투자결과_리턴_테스트() throws Exception {

        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemId", 2);
        jsonObject.put("investingAmount", 10000);
        String jsonBody = jsonObject.toString();

        //when
        //then
        mockMvc.perform(post("/api/investment")
                .content(jsonBody)
                .header("X-USER-ID", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("sold-out")));
    }
}
