package com.flab.modu.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.market.domain.Market;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.common.UserConstant;
import com.flab.modu.users.service.LoginService;
import java.io.IOException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Product Controller 테스트")
@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class})
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private LoginService loginService;

    private final Market preparedMarket = Market.builder().sellerId("testUser").build();

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
    @DisplayName("상품 생성에 성공한다.")
    public void givenValidData_whenCreatingProduct_then200OK() throws Exception {
        //given
        MockMultipartFile multipartFile = createMultipartFile();
        ProductDto.CreateRequest createRequest = createProductCreateRequest(1L, "상품1", 10000, 20,
            ProductStatus.ACTIVE);
        ProductDto.CreateResponse createResponse = createProductCreateResponse(createRequest,
            getImageByteArr(multipartFile));
        given(productService.createProduct(any(ProductDto.CreateRequest.class),
            any(MultipartFile.class), any(String.class)))
            .willReturn(createResponse);

        //when
        ResultActions result = mockMvc.perform(multipart("/products")
            .file(multipartFile)
            .file(new MockMultipartFile("product", "product", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createRequest)))
            .session(session)
        );

        //then
        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(CoreMatchers.equalTo(createRequest.getName())))
            .andExpect(jsonPath("$.price").value(CoreMatchers.equalTo(createRequest.getPrice())))
            .andExpect(jsonPath("$.status").value(
                CoreMatchers.equalTo(createRequest.getStatus().getCode())))
            .andExpect(jsonPath("$.stock").value(CoreMatchers.equalTo(createRequest.getStock())))
            .andExpect(jsonPath("$.market").value(CoreMatchers.notNullValue()));

        then(productService).should().createProduct(refEq(createRequest),
            refEq(multipartFile),
            refEq((String) session.getAttribute(UserConstant.EMAIL)));

        //document
        result.andDo(document("create-product",
            requestPartFields("product",
                fieldWithPath("marketId").type(JsonFieldType.NUMBER).description("마켓고유아이디"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품가격"),
                fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고"),
                fieldWithPath("status").type(JsonFieldType.STRING)
                    .description("상태(활성:ACTIVE/비활성:DISABLE)")
            ),
            responseFields(
                fieldWithPath("id").optional().type(JsonFieldType.NUMBER).description("상품아이디"),
                fieldWithPath("market.id").optional().type(JsonFieldType.NUMBER)
                    .description("마켓아이디"),
                fieldWithPath("market.name").optional().type(JsonFieldType.STRING)
                    .description("마켓명"),
                fieldWithPath("market.sellerId").optional().type(JsonFieldType.STRING)
                    .description("판매자아이디"),
                fieldWithPath("market.status").optional().type(JsonFieldType.STRING)
                    .description("마켓상태"),
                fieldWithPath("market.url").optional().type(JsonFieldType.STRING)
                    .description("마켓주소"),
                fieldWithPath("market.createdAt").optional().type(JsonFieldType.STRING)
                    .description("마켓생성날짜"),
                fieldWithPath("market.modifiedAt").optional().type(JsonFieldType.STRING)
                    .description("마켓수정날짜"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고"),
                fieldWithPath("status").type(JsonFieldType.STRING)
                    .description("상태(활성:ACTIVE/비활성:DISABLE)"),
                fieldWithPath("image").type(JsonFieldType.STRING).description("이미지바이너리")
            )
        ));
    }

    private ProductDto.CreateRequest createProductCreateRequest(Long marketId, String name,
        Integer price, Integer stock, ProductStatus status) {
        return ProductDto.CreateRequest.builder()
            .marketId(marketId)
            .price(price)
            .name(name)
            .stock(stock)
            .status(status)
            .build();
    }

    private ProductDto.CreateResponse createProductCreateResponse(
        ProductDto.CreateRequest createRequest, byte[] image) {
        Product product = createRequest.toEntity(preparedMarket, image);
        return ProductDto.CreateResponse.builder().product(product).build();
    }

    private MockMultipartFile createMultipartFile() {
        return new MockMultipartFile("image", "이미지.png", "image/png", "<png-data>".getBytes());
    }

    private byte[] getImageByteArr(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            return new byte[]{};
        }
    }
}