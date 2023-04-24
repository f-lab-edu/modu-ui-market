package com.flab.modu.product.domain.entity;

import com.flab.modu.global.domain.BaseTimeEntity;
import com.flab.modu.market.domain.Market;
import com.flab.modu.product.domain.common.ProductStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Market market;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer price;

    @Lob
    @Column(length = 10240)
    private byte[] image;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Builder
    public Product(Market market, String name, Integer stock, Integer price, byte[] image,
        ProductStatus status) {
        this.market = market;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.image = image;
        this.status = status;
    }

    public void update(String name, Integer stock, Integer price, byte[] image, ProductStatus status){
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.image = image;
        this.status = status;
    }

    public void sell(int orderAmount) {
        this.stock -= orderAmount;
    }
}
