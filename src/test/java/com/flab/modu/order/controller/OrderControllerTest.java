package com.flab.modu.order.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.order.controller.OrderDto.OrderRequest;
import com.flab.modu.order.service.OrderCallService;
import com.flab.modu.order.service.OrderService;
import com.flab.modu.users.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Order Controller 테스트")
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderCallService orderCallService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String USER_EMAIL = "test@modu.com";

    @Test
    @DisplayName("상품주문 - 상품주문에 성공한다.")
    void createOrder_successful() throws Exception {
        OrderRequest orderRequest = createOrderRequest(1);

        willAnswer(invocation -> USER_EMAIL).given(loginService).getLoginUser();
        given(orderCallService.callOrder(orderRequest, USER_EMAIL)).willReturn(1L);

        mockMvc.perform(post("/orders")
                .header("email", USER_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("orders/create/success",
                requestFields(
                    fieldWithPath("productId").type(JsonFieldType.NUMBER)
                        .description("구매할 상품의 ID"),
                    fieldWithPath("amount").type(JsonFieldType.NUMBER)
                        .description("주문수량"),
                    fieldWithPath("deliveryMassage").type(JsonFieldType.STRING)
                        .description("배송메시지")
                        .optional(),
                    fieldWithPath("receiverTel").type(JsonFieldType.STRING)
                        .description("수령자 번호")
                        .optional()
                )
            ));

        then(orderCallService).should().callOrder(refEq(orderRequest), anyString());
    }

    private OrderDto.OrderRequest createOrderRequest(int orderAmount) {
        return OrderDto.OrderRequest.builder()
            .productId(1L)
            .amount(orderAmount)
            .build();
    }
}