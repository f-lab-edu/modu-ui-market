package com.flab.modu.order.domain.entity;

import com.flab.modu.product.domain.entity.Product;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int amount;

    @Builder
    public OrderProduct(Long id, Order order, Product product, int amount) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.amount = amount;
    }
}
