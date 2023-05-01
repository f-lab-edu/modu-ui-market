package com.flab.modu.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.flab.modu.global.config.JpaConfig;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.domain.MarketStatus;
import com.flab.modu.market.repository.MarketRepository;
import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.controller.OrderDto.OrderRequest;
import com.flab.modu.order.repository.OrderRepository;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.repository.ProductRepository;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.repository.UserRepository;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;

@DisplayName("주문서비스 재고관리 테스트")
@Import(value = JpaConfig.class)
@SpringBootTest
public class OrderServiceConcurrencyTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    final String EMAIL = "test@test.com";

    private Product savedProduct;

    @BeforeEach
    void setUp() {
        User user = User.builder()
            .email(EMAIL)
            .name("modu")
            .role(UserRole.BUYER)
            .password("test12345")
            .build();

        userRepository.save(user);

        Market market = Market.builder()
            .sellerId("sellerId")
            .url("url")
            .name("name")
            .status(MarketStatus.ACTIVE)
            .build();

        marketRepository.save(market);

        savedProduct = Product.builder().market(market)
            .stock(20)
            .image(new byte[]{})
            .name("상품명")
            .status(ProductStatus.ACTIVE)
            .price(10000)
            .build();

        productRepository.save(savedProduct);
    }

    @Test
    @DisplayName("낙관적 락 동시 주문 테스트")
    void concurrency_test1() throws Exception {
        // given
        int orderAmount = 1;
        OrderRequest orderRequest = createOrderRequest(orderAmount);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // when
        Future<?> future = executorService.submit(
            () -> {
                orderService.createOrder(orderRequest, EMAIL);
            });
        Future<?> future2 = executorService.submit(
            () -> {
                orderService.createOrder(orderRequest, EMAIL);
            });
        Future<?> future3 = executorService.submit(
            () -> {
                orderService.createOrder(orderRequest, EMAIL);
            });

        Exception result = new Exception();

        try {
            future.get();
            future2.get();
            future3.get();
        } catch (ExecutionException e) {
            result = (Exception) e.getCause();
        }

        // then
        assertTrue(result instanceof OptimisticLockingFailureException);
        Product product = productService.getProduct(savedProduct.getId());
        assertEquals(product.getStock(), savedProduct.getStock() - orderAmount);
    }

    private OrderDto.OrderRequest createOrderRequest(int orderAmount) {
        return OrderDto.OrderRequest.builder()
            .productId(savedProduct.getId())
            .amount(orderAmount)
            .build();
    }
}
