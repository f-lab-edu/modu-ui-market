package com.flab.modu.product.domain.common;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ACTIVE("ACTIVE"), DISABLE("DISABLE");

    private String code;

    ProductStatus(String code) {
        this.code = code;
    }

    public static ProductStatus of(String code) {
        if (code == null) {
            throw new IllegalArgumentException();
        }

        for (ProductStatus productStatus : ProductStatus.values()) {
            if (productStatus.code.equals(code)) {
                return productStatus;
            }
        }

        throw new IllegalArgumentException("일치하는 상품상태 코드가 없습니다.");
    }
}
