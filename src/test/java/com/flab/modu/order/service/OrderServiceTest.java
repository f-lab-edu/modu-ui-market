package com.flab.modu.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import com.flab.modu.market.domain.Market;
import com.flab.modu.market.domain.MarketStatus;
import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.domain.common.OrderStatus;
import com.flab.modu.order.domain.entity.Order;
import com.flab.modu.order.repository.OrderRepository;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.exception.InsufficientStockException;
import com.flab.modu.product.exception.NotExistProductException;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("정상적으로 주문 생성에 성공한다.")
    public void createOrder_successful() throws Exception {
        // give
        OrderDto.OrderRequest orderRequest = createOrderRequest(1);
        Product product = createProduct(10);
        User buyer = createBuyer();
        Order order = createOrder(buyer);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(buyer));
        given(productService.getProduct(anyLong())).willReturn(product);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        Long orderId = orderService.createOrder(orderRequest, buyer.getEmail());

        // then
        then(userRepository).should().findByEmail(anyString());
        then(productService).should().getProduct(anyLong());
        then(orderRepository).should().save(any(Order.class));
    }

    @Test
    @DisplayName("존재하지 않는 유저의 주문은 실패한다.")
    public void notExistEmail_createOrder_failure() throws Exception {
        // give
        OrderDto.OrderRequest orderRequest = createOrderRequest(1);
        given(userRepository.findByEmail(anyString())).willThrow(new NotExistedUserException());

        // when
        assertThrows(NotExistedUserException.class,
            () -> orderService.createOrder(orderRequest, "noExistUserEmail"));

        // then
        then(userRepository).should().findByEmail(anyString());
        then(userRepository).shouldHaveNoMoreInteractions();
        then(productService).shouldHaveNoInteractions();
        then(orderRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("존재하지 않는 상품의 주문은 실패한다.")
    public void notExistProduct_createOrder_failure() throws Exception {
        // give
        OrderDto.OrderRequest orderRequest = createOrderRequest(1);
        User buyer = createBuyer();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(buyer));
        given(productService.getProduct(anyLong())).willThrow(
            new NotExistProductException());

        // when
        assertThrows(NotExistProductException.class,
            () -> orderService.createOrder(orderRequest, buyer.getEmail()));

        // then
        then(userRepository).should().findByEmail(anyString());
        then(productService).should().getProduct(anyLong());
        then(userRepository).shouldHaveNoMoreInteractions();
        then(orderRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("상품재고가 다 떨어졌다면 상품의 주문은 실패한다.")
    public void zeroStockProduct_createOrder_failure() throws Exception {
        // give
        OrderDto.OrderRequest orderRequest = createOrderRequest(10);
        Product product = createProduct(0);
        User buyer = createBuyer();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(buyer));
        given(productService.getProduct(anyLong())).willReturn(product);
        willThrow(new InsufficientStockException("상품의 재고가 부족합니다.")).given(productService)
            .sellProduct(eq(product), anyInt());

        // when
        Throwable exception = assertThrows(
            InsufficientStockException.class,
            () -> orderService.createOrder(orderRequest, buyer.getEmail()));
        assertEquals("상품의 재고가 부족합니다.", exception.getMessage());

        // then
        then(userRepository).should().findByEmail(anyString());
        then(productService).should().getProduct(anyLong());
        then(productService).should().sellProduct(eq(product), anyInt());
        then(orderRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("주문수량보다 상품재고가 부족하면 상품의 주문은 실패한다.")
    public void insufficientStock_createOrder_failure() throws Exception {
        // give
        OrderDto.OrderRequest orderRequest = createOrderRequest(10);
        Product product = createProduct(2);
        User buyer = createBuyer();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(buyer));
        given(productService.getProduct(anyLong())).willReturn(product);
        willThrow(new InsufficientStockException("주문 수량이 상품의 재고보다 많습니다.")).given(productService)
            .sellProduct(eq(product), anyInt());

        // when
        Throwable exception = assertThrows(
            InsufficientStockException.class,
            () -> orderService.createOrder(orderRequest, buyer.getEmail()));
        assertEquals("주문 수량이 상품의 재고보다 많습니다.", exception.getMessage());

        // then
        then(userRepository).should().findByEmail(anyString());
        then(productService).should().getProduct(anyLong());
        then(productService).should().sellProduct(eq(product), anyInt());
        then(orderRepository).shouldHaveNoInteractions();
    }


    private OrderDto.OrderRequest createOrderRequest(int orderAmount) {
        return OrderDto.OrderRequest.builder()
            .productId(1L)
            .amount(orderAmount)
            .build();
    }

    private User createBuyer() {
        return User.builder()
            .email("test@modu.com")
            .name("테스트네임")
            .password("test12345@")
            .role(UserRole.BUYER)
            .phoneNumber("01012345678")
            .build();
    }

    private Product createProduct(int stock) {
        return Product.builder()
            .name("테스트상품")
            .stock(stock)
            .status(ProductStatus.ACTIVE)
            .market(createMarket())
            .build();
    }

    private Market createMarket() {
        return Market.builder()
            .name("테스트마켓")
            .url("testUrl")
            .sellerId("testId")
            .status(MarketStatus.ACTIVE)
            .build();
    }

    private Order createOrder(User buyer) {
        return Order.builder()
            .buyer(buyer)
            .status(OrderStatus.PAYMENT_CONFIRMED)
            .build();
    }
}