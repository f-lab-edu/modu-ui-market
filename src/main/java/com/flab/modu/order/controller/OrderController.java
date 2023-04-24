package com.flab.modu.order.controller;

import com.flab.modu.global.annotation.CurrentUser;
import com.flab.modu.global.annotation.LoginCheck;
import com.flab.modu.order.service.OrderService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @LoginCheck
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(@RequestBody @Valid OrderDto.OrderRequest orderRequest, @CurrentUser String email) {
        return orderService.createOrder(orderRequest, email);
    }
}
