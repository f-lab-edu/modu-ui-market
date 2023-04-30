package com.flab.modu.order.domain.entity;

import com.flab.modu.global.domain.BaseTimeEntity;
import com.flab.modu.order.domain.common.OrderStatus;
import com.flab.modu.users.domain.entity.User;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BUYER_ID")
    private User buyer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Set<OrderProduct> orderProducts = new HashSet<>();

    @Lob
    private String deliveryMassage;

    private String receiverTel;

    @Builder
    public Order(Long id, User buyer, OrderStatus status,
        String deliveryMassage, String receiverTel) {
        this.id = id;
        this.buyer = buyer;
        this.status = status;
        this.deliveryMassage = deliveryMassage;
        this.receiverTel = receiverTel;
    }

    public void addProduct(OrderProduct product) {
        orderProducts.add(product);
    }
}
