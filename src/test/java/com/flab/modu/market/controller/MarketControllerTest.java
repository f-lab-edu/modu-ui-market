package com.flab.modu.market.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.market.controller.MarketDto.CreateResponse;
import com.flab.modu.market.service.MarketService;
import com.flab.modu.users.domain.common.UserConstant;
import com.flab.modu.users.service.LoginService;
import java.time.LocalDateTime;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("Market Controller 테스트")
@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class})
@WebMvcTest(MarketController.class)
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarketService marketService;

    @MockBean
    private LoginService loginService;

    @BeforeEach
    public void setUp() throws Exception {
        String loginEmail = "user@test.com";
        session = new MockHttpSession();
        session.setAttribute(UserConstant.EMAIL, loginEmail);
    }

    @AfterEach
    public void clean() {
        session.clearAttributes();
    }

    @Test
    @DisplayName("마켓 생성에 성공한다.")
    public void givenValidData_whenCreatingMarket_then200OK() throws Exception {
        //given
        MarketDto.CreateRequest createRequest = createMarketCreateRequest("MarketUrl",
            "Market Name");
        MarketDto.CreateResponse createResponse = createMarketCreateResponse(createRequest);
        given(marketService.createMarket(any(MarketDto.CreateRequest.class),
            any(String.class))).willReturn(
            createResponse);

        //when
        ResultActions result = mockMvc.perform(post("/markets")
            .session(session)
            .content(objectMapper.writeValueAsString(createRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(CoreMatchers.equalTo(createRequest.getName())))
            .andExpect(jsonPath("$.url").value(CoreMatchers.equalTo(createRequest.getUrl())))
            .andExpect(jsonPath("$.sellerId").value(
                CoreMatchers.equalTo((String) session.getAttribute(UserConstant.EMAIL))));
        then(marketService).should().createMarket(refEq(createRequest),
            refEq((String) session.getAttribute(UserConstant.EMAIL)));

        //document
        result.andDo(document("create-market",
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("마켓명"),
                fieldWithPath("url").type(JsonFieldType.STRING).description("마켓주소")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("마켓아이디"),
                fieldWithPath("sellerId").type(JsonFieldType.STRING).description("판매자아이디"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("마켓명"),
                fieldWithPath("url").type(JsonFieldType.STRING).description("마켓주소"),
                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일시"),
                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정일시")
            )
        ));
    }

    @Test
    @DisplayName("입력값 최대 길이로 마켓 생성에 성공한다.")
    public void givenMaximumLengthData_whenCreatingMarket_then200OK() throws Exception {
        //given
        String sellerId = RandomStringUtils.randomAlphabetic(50);
        String marketName = RandomStringUtils.random(200, "가나다라마바사아자차카타파하");
        String url = RandomStringUtils.randomAlphabetic(100);

        Assertions.assertThat(sellerId.length()).isEqualTo(50);
        Assertions.assertThat(marketName.length()).isEqualTo(200);
        Assertions.assertThat(url.length()).isEqualTo(100);

        MarketDto.CreateRequest createRequest = createMarketCreateRequest(url, marketName);
        MarketDto.CreateResponse createResponse = createMarketCreateResponse(createRequest);
        given(marketService.createMarket(any(MarketDto.CreateRequest.class),
            any(String.class))).willReturn(
            createResponse);

        //when
        ResultActions result = mockMvc.perform(post("/markets")
            .session(session)
            .content(objectMapper.writeValueAsString(createRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.url").exists())
            .andExpect(jsonPath("$.sellerId").exists());
        then(marketService).should().createMarket(refEq(createRequest),
            refEq((String) session.getAttribute(UserConstant.EMAIL)));
    }

    @Test
    @DisplayName("입력값 길이 초과로 마켓 생성에 실패한다.")
    public void givenInvalidLengthData_whenCreatingMarket_then400BadRequestWithMessage()
        throws Exception {
        //given
        String sellerId = RandomStringUtils.randomAlphabetic(51);
        String marketName = RandomStringUtils.random(201, "가나다라마바사아자차카타파하");
        String url = RandomStringUtils.randomAlphabetic(101);

        MarketDto.CreateRequest createMarketRequest = createMarketCreateRequest(url, marketName);

        //when
        ResultActions result = mockMvc.perform(post("/markets")
            .session(session)
            .content(objectMapper.writeValueAsString(createMarketRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists());
        then(marketService).shouldHaveNoInteractions();
    }

    @ParameterizedTest
    @ValueSource(strings = {"한글입력", "include blank", "url!!"})
    @DisplayName("잘못된 형식의 URL로 마켓 생성 실패한다.")
    public void givenInvalidUrl_whenCreatingMarket_then400BadRequestWithMessage(String invalidUrl)
        throws Exception {
        //given
        MarketDto.CreateRequest createRequest = createMarketCreateRequest(invalidUrl, "마켓명");

        //when
        ResultActions result = mockMvc.perform(post("/markets")
            .session(session)
            .content(objectMapper.writeValueAsString(createRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists());
        then(marketService).shouldHaveNoInteractions();
    }

    private MarketDto.CreateRequest createMarketCreateRequest(String url, String name) {
        return MarketDto.CreateRequest.builder()
            .name(name)
            .url(url)
            .build();
    }

    private CreateResponse createMarketCreateResponse(MarketDto.CreateRequest createRequest) {
        MarketDto.CreateResponse createResponse = MarketDto.CreateResponse.builder()
            .market(createRequest.toEntity((String) session.getAttribute(UserConstant.EMAIL)))
            .build();
        createResponse.setId(1L);
        createResponse.setCreatedAt(LocalDateTime.now());
        createResponse.setModifiedAt(LocalDateTime.now());
        return createResponse;
    }
}