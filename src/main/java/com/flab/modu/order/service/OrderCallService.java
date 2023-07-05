package com.flab.modu.order.service;

import com.flab.modu.order.controller.OrderDto;
import com.flab.modu.order.exception.OrderFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderCallService {

    private final OrderService orderService;

    public Long callOrder(OrderDto.OrderRequest orderRequest, String userEmail) {
        try {
            return orderService.createOrder(orderRequest, userEmail);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OrderFailureException();
        }
    }
}
