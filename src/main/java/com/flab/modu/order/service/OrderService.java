package com.flab.modu.order.service;

import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.controller.OrderDto.OrderRequest;
import com.flab.modu.order.domain.entity.Order;
import com.flab.modu.order.domain.entity.OrderProduct;
import com.flab.modu.order.exception.OrderFailureException;
import com.flab.modu.order.repository.OrderRepository;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.repository.UserRepository;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductService productService;

    private final StockManagementService stockManagementService;

    private static final int OPTIMISTIC_LOOP_COUNT = 5;

    @Transactional
    public Long createOrder(OrderDto.OrderRequest orderRequest, String userEmail) {

        User buyer = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new NotExistedUserException());

        Order order = orderRequest.toEntity(buyer);

        OrderProduct orderProduct = OrderProduct.builder()
            .order(order)
            .amount(orderRequest.getAmount())
            .build();

//        IntStream.range(0, OPTIMISTIC_LOOP_COUNT)
//            .filter(i -> stockManagement(orderRequest, orderProduct))
//            .findFirst();

        Product product = productService.getProduct(orderRequest.getProductId());

        productService.sellProduct(product, orderRequest.getAmount());

        orderProduct.setProduct(product);

        order.addProduct(orderProduct);

        return orderRepository.save(order).getId();
    }

    private boolean stockManagement(OrderRequest orderRequest, OrderProduct orderProduct) {
        try {
            Product product = stockManagementService.subtractStock(orderRequest.getProductId(),
                orderRequest.getAmount());

            orderProduct.setProduct(product);

            return true;
        } catch (ObjectOptimisticLockingFailureException e) {
            waitFailedUpdate();
            return false;
        }
    }

    private void waitFailedUpdate() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
