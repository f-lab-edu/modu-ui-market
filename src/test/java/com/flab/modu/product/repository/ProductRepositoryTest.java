package com.flab.modu.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.modu.global.config.JpaConfig;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.domain.MarketStatus;
import com.flab.modu.market.repository.MarketRepository;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("Product JPA 연결 테스트")
@Import(value = JpaConfig.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private ProductRepository productRepository;

    private Market savedMarket;

    private Product savedProduct;

    @BeforeEach
    private void beforeEach() {
        Market market = Market.builder()
            .sellerId("sellerId")
            .url("url")
            .name("name")
            .status(MarketStatus.ACTIVE)
            .build();

        savedMarket = marketRepository.save(market);

        savedProduct = insertProductData(savedMarket, 20, "상품명",10000,ProductStatus.ACTIVE,new byte[]{});
    }

    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() throws Exception {
        //given

        //when
        List<Product> products = productRepository.findAll();

        //then
        assertThat(products)
            .isNotNull()
            .hasSize(1);
    }

    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() throws Exception {
        //given
        long prevCount = productRepository.count();

        //when
        productRepository.save(
            new Product(savedMarket,"상품명2",1, 150000, new byte[]{}, ProductStatus.ACTIVE)
        );

        //then
        assertThat(productRepository.count()).isEqualTo(prevCount + 1);
    }

    @Test
    @DisplayName("update 테스트")
    void givenTestData_whenUpdating_thenWorksFine() throws Exception {
        //given
        Product product = productRepository.findById(savedProduct.getId()).orElseThrow();
        Integer updatePrice = 1000;
        product.update(product.getName(), product.getStock(), updatePrice ,product.getImage(),product.getStatus());

        //when
        Product updatedProduct = productRepository.saveAndFlush(product);

        //then
        assertThat(updatedProduct).hasFieldOrPropertyWithValue("price", updatePrice);
    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() throws Exception {
        //given
        Product product = productRepository.findById(savedProduct.getId()).orElseThrow();
        long count = productRepository.count();

        // when
        productRepository.delete(product);

        //then
        assertThat(productRepository.count()).isEqualTo(count - 1);
    }

    private Product insertProductData(Market market, Integer stock, String name, Integer price, ProductStatus status, byte[] image) {
        Product product = Product.builder().market(market)
            .stock(stock)
            .image(image)
            .name(name)
            .status(status)
            .price(price)
            .build();

        return productRepository.save(product);
    }

}
