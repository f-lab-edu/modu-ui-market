package com.flab.modu.product.controller;

import com.flab.modu.market.domain.Market;
import com.flab.modu.product.domain.common.ProductStatus;
import com.flab.modu.product.domain.entity.Product;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class ProductDto {

    @Getter
    public static class CreateRequest {

        @NotNull(message = "마켓아이디를 입력해주세요.")
        private Long marketId;

        @NotNull(message = "상품명을 입력해주세요.")
        @Length(min = 1, max = 200, message = "상품명 길이를 확인해주세요.")
        private String name;

        @NotNull(message = "재고를 입력해주세요.")
        @Min(value = 0, message = "재고를 1개 이상 입력해주세요.")
        @Max(value = 10_000, message = "재고를 10,000개 이하로 입력해주세요.")
        private Integer stock;

        @NotNull(message = "가격을 입력해주세요.")
        @Min(value = 0, message = "가격을 0원 이상 입력해주세요.")
        @Max(value = 100_000_000, message = "가격을 100,000,000원 이하로 입력해주세요.")
        private Integer price;

        @NotNull(message = "상품 상태를 입력해주세요.")
        private ProductStatus status;

        @Builder
        public CreateRequest(Long marketId, String name, Integer stock, Integer price,
            ProductStatus status) {
            this.marketId = marketId;
            this.name = name;
            this.stock = stock;
            this.price = price;
            this.status = status;
        }

        public Product toEntity(Market market, byte[] image) {
            return Product.builder().market(market)
                .stock(this.stock)
                .image(image)
                .name(this.name)
                .status(this.status)
                .price(this.price)
                .build();
        }
    }

    @Getter
    public static class CreateResponse {

        private Long id;
        private Market market;
        private String name;
        private Integer price;
        private byte[] image;
        private Integer stock;
        private ProductStatus status;

        @Builder
        public CreateResponse(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.image = product.getImage();
            this.stock = product.getStock();
            this.status = product.getStatus();
            this.market = product.getMarket();
        }
    }
}
