package com.flab.modu.order.repository;

import com.flab.modu.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
