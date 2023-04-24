package com.flab.modu.order.service;

import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.domain.entity.Order;
import com.flab.modu.order.domain.entity.OrderProduct;
import com.flab.modu.order.repository.OrderRepository;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.exception.NotExistProductException;
import com.flab.modu.product.repository.ProductRepository;
import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductService productService;

    public Long createOrder(OrderDto.OrderRequest orderRequest, String userEmail) {

        User buyer = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new NotExistedUserException());

        Order order = orderRequest.toEntity(buyer);

        Product product = productService.getProduct(orderRequest.getProductId());

        productService.sellProduct(product, orderRequest.getAmount());

        OrderProduct orderProduct = OrderProduct.builder()
            .order(order)
            .product(product)
            .amount(orderRequest.getAmount())
            .build();

        order.addProduct(orderProduct);

        return orderRepository.save(order).getId();
    }
}
