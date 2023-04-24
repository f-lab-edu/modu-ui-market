package com.flab.modu.order.controller;

import com.flab.modu.order.domain.common.OrderStatus;
import com.flab.modu.order.domain.entity.Order;
import com.flab.modu.users.domain.entity.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderRequest {

        @NotBlank(message = "상품을 선택해주세요.")
        private Long productId;

        @NotBlank(message = "개수를 입력해주세요.")
        private int amount;

        private String deliveryMassage;

        @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
        private String receiverTel;

        @Builder
        public OrderRequest(Long productId, int amount, String deliveryMassage,
            String receiverTel) {
            this.productId = productId;
            this.amount = amount;
            this.deliveryMassage = deliveryMassage;
            this.receiverTel = receiverTel;
        }

        public Order toEntity(User buyer) {
            return Order.builder()
                .buyer(buyer)
                .status(OrderStatus.PAYMENT_CONFIRMED)
                .deliveryMassage(deliveryMassage)
                .receiverTel(receiverTel)
                .build();
        }
    }
}
