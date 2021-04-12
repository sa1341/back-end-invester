package com.kakaopay.investment.controller;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.kakaopay.investment.doc.ApiDocumentUtils.getDocumentRequest;
import static com.kakaopay.investment.doc.ApiDocumentUtils.getDocumentResponse;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class InvestmentRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    WebApplicationContext context;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    @DisplayName("회원이 특정 투자상품에 투자 요청을 정상적으로 처리한다.")
    @Test
    public void 투자결과_리턴_테스트() throws Exception {

        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemId", 1);
        jsonObject.put("investingAmount", 100000);
        String jsonBody = jsonObject.toString();

        //when
        //then
        mockMvc.perform(post("/api/investment")
                .content(jsonBody)
                .header("X-USER-ID", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("success")))
                .andDo(document("investment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("투자상품"),
                                fieldWithPath("investingAmount").type(JsonFieldType.NUMBER).description("투자 금액")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("투자 결과")
                        )
                ));
    }
}
