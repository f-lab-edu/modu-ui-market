package com.flab.modu.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.market.domain.Market;
import com.flab.modu.market.exception.MarketNoPermissionException;
import com.flab.modu.market.exception.MarketNotFoundException;
import com.flab.modu.market.repository.MarketRepository;
import com.flab.modu.product.controller.ProductDto;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Product Service 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MarketRepository marketRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("정상적으로 마켓생성에 성공한다.")
    public void givenTestData_whenCreatingMarket_thenSuccess() {
        // given
        String sellerId = "sellerId";
        ProductDto.CreateRequest createRequest = createProductDto(1L, "상품명", 25000, 100,
            ProductStatus.ACTIVE);
        Market market = Market.builder().sellerId(sellerId).build();

        given(marketRepository.findById(any(Long.class))).willReturn(
            Optional.of(market));
        given(productRepository.save(any(Product.class))).willReturn(
            createRequest.toEntity(market, null));

        // when
        productService.createProduct(createRequest, null, sellerId);

        // then
        then(marketRepository).should().findById(any(Long.class));
        then(productRepository).should().save(any(Product.class));
    }

    @Test
    @DisplayName("존재하지 않는 마켓정보로 상품 생성에 실패한다.")
    public void givenNotExistMarketId_whenCreatingProduct_thenThrowMarketNotFoundException() {
        // given
        String sellerId = "sellerId";
        ProductDto.CreateRequest createRequest = createProductDto(1234L, "상품명", 25000, 100,
            ProductStatus.ACTIVE);
        Market market = Market.builder().sellerId(sellerId).build();

        given(marketRepository.findById(any(Long.class))).willReturn(
            Optional.empty());

        // when
        Assertions.assertThrows(MarketNotFoundException.class,
            () -> productService.createProduct(createRequest, null, sellerId));

        // then
        then(marketRepository).should().findById(any(Long.class));
        then(marketRepository).shouldHaveNoMoreInteractions();
        then(productRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("내 소유가 아닌 마켓정보로 상품 생성에 실패한다.")
    public void givenNotMineMarket_whenCreatingProduct_thenThrowMarketNoPermissionException()
        throws Exception {
        // given
        String sellerId = "sellerId";
        String anotherSellerId = "hello";
        ProductDto.CreateRequest createRequest = createProductDto(1234L, "상품명", 25000, 100,
            ProductStatus.ACTIVE);
        Market market = Market.builder().sellerId(sellerId).build();

        given(marketRepository.findById(any(Long.class))).willReturn(
            Optional.of(market));

        // when
        Assertions.assertThrows(MarketNoPermissionException.class,
            () -> productService.createProduct(createRequest, null, anotherSellerId));

        // then
        then(marketRepository).should().findById(any(Long.class));
        then(marketRepository).shouldHaveNoMoreInteractions();
        then(productRepository).shouldHaveNoInteractions();
    }

    private ProductDto.CreateRequest createProductDto(Long marketId, String name, Integer price,
        Integer stock, ProductStatus status) {
        return ProductDto.CreateRequest.builder()
            .marketId(marketId)
            .price(price)
            .name(name)
            .stock(stock)
            .status(status)
            .build();
    }
}
