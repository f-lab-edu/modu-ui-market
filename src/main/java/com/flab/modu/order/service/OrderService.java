package com.flab.modu.order.service;

import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.domain.entity.Order;
import com.flab.modu.order.exception.OrderFailureException;
import com.flab.modu.order.repository.OrderRepository;
import com.flab.modu.product.repository.ProductRepository;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OptimisticOrderService optimisticOrderService;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductService productService;

    private final ProductRepository productRepository;

    @Transactional
    public Long callOrder(OrderDto.OrderRequest orderRequest, String userEmail) {
        try {
            return optimisticOrderService.createOrder(orderRequest, userEmail);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OrderFailureException();
        }
    }

    @Transactional
    public Long createOrder(OrderDto.OrderRequest orderRequest, String userEmail) {
        User buyer = userRepository.findByEmail(userEmail)
            .orElseThrow(NotExistedUserException::new);

        Order order = orderRequest.toEntity(buyer);
    }
}
