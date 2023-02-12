package com.flab.modu.domain.market.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createMarket() throws Exception {
        MarketDto.CreateRequest createMarketRequest = MarketDto.CreateRequest.builder()
            .sellerId("yujin")
            .name("Yujin's Market")
            .url("yujinMarket")
            .build();

        String json = objectMapper.writeValueAsString(createMarketRequest);

        mockMvc.perform(post("/markets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(CoreMatchers.equalTo("Yujin's Market")))
            .andExpect(jsonPath("$.url").value(CoreMatchers.equalTo("yujinMarket")))
            .andExpect(jsonPath("$.sellerId").value(CoreMatchers.equalTo("yujin")))
            .andExpect(jsonPath("$.id").value(CoreMatchers.notNullValue()))
            .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
            .andExpect(jsonPath("$.modifiedAt").value(CoreMatchers.notNullValue()))
        ;
    }

    @Test
    public void createMarketByMaximumLangthParams() throws Exception {
        String sellerId = "01234567890123456789012345678901234567890123456789";
        String marketName = "";
        for (int i = 0; i < 200; i++) {
            marketName += "일";
        }
        String url = "";
        for (int i = 0; i < 10; i++) {
            url += "ThisIsUrls";
        }

        Assertions.assertThat(sellerId.length()).isEqualTo(50);
        Assertions.assertThat(marketName.length()).isEqualTo(200);
        Assertions.assertThat(url.length()).isEqualTo(100);

        MarketDto.CreateRequest createMarketRequest = MarketDto.CreateRequest.builder()
            .sellerId(sellerId)
            .name(marketName)
            .url(url)
            .build();

        String json = objectMapper.writeValueAsString(createMarketRequest);

        mockMvc.perform(post("/markets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(CoreMatchers.notNullValue()))
            .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
            .andExpect(jsonPath("$.modifiedAt").value(CoreMatchers.notNullValue()))
        ;
    }

    @Test
    public void failCreatingMarketByOverMaximumLangthParams() throws Exception {
        String sellerId = "01234567890123456789012345678901234567890123456789";
        sellerId += "1";

        String marketName = "";
        for (int i = 0; i < 200; i++) {
            marketName += "일";
        }
        marketName += "1";

        String url = "";
        for (int i = 0; i < 10; i++) {
            url += "ThisIsUrls";
        }
        url += "1";

        Assertions.assertThat(sellerId.length()).isEqualTo(51);
        Assertions.assertThat(marketName.length()).isEqualTo(201);
        Assertions.assertThat(url.length()).isEqualTo(101);

        MarketDto.CreateRequest createMarketRequest = MarketDto.CreateRequest.builder()
            .sellerId(sellerId)
            .name(marketName)
            .url(url)
            .build();

        String json = objectMapper.writeValueAsString(createMarketRequest);

        mockMvc.perform(post("/markets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
        ;
    }

    @Test
    public void failCreatingMarketByWrongParams() throws Exception {
        String sellerId = "yujin";
        String marketName = "이름은 아무거나";
        String url1 = "한글입력";
        String url2 = "include blank";
        String url3 = "url!!";

        String json = objectMapper.writeValueAsString(
            MarketDto.CreateRequest.builder()
                .sellerId(sellerId)
                .name(marketName)
                .url(url1)
                .build()
        );
        mockMvc.perform(post("/markets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
        ;

        json = objectMapper.writeValueAsString(
            MarketDto.CreateRequest.builder()
                .sellerId(sellerId)
                .name(marketName)
                .url(url2)
                .build()
        );
        mockMvc.perform(post("/markets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
        ;

        json = objectMapper.writeValueAsString(
            MarketDto.CreateRequest.builder()
                .sellerId(sellerId)
                .name(marketName)
                .url(url3)
                .build()
        );
        mockMvc.perform(post("/markets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
        ;
    }
}