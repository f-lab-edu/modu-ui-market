package com.flab.modu.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.flab.modu.global.config.JpaConfig;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.domain.MarketStatus;
import com.flab.modu.market.repository.MarketRepository;
import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.controller.OrderDto.OrderRequest;
import com.flab.modu.order.exception.OrderFailureException;
import com.flab.modu.order.repository.OrderRepository;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.repository.ProductRepository;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
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

    @Autowired
    OrderCallService orderCallService;

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
    @DisplayName("비관적 락 동시성 테스트")
    void pessimistic_write_transaction_concurrency_test() throws Exception {
        // given
        int orderAmount = 1;
        int threadCount = 19;
        OrderRequest orderRequest = createOrderRequest(orderAmount);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        List<Future<Long>> futures = new ArrayList<>();
        for (int i=0; i<threadCount; i++) {
            // callable 객체를 통해 어떤 일을 수행할지 결정한다.
            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    System.out.println("callOrder!!");
                    return orderCallService.callOrder(orderRequest, EMAIL);
                }
            };

            // 생성된 callable들을 threadpool에서 수행시킴
            futures.add(threadPool.submit(callable));
        }

        try {
            threadPool.shutdown();
            for (Future<Long> future : futures) {
                future.get();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // then
        Product product = productService.getProduct(savedProduct.getId());
        assertEquals(product.getStock(), savedProduct.getStock() - orderAmount*threadCount);
    }

    private OrderDto.OrderRequest createOrderRequest(int orderAmount) {
        return OrderDto.OrderRequest.builder()
            .productId(savedProduct.getId())
            .amount(orderAmount)
            .build();
    }
}
