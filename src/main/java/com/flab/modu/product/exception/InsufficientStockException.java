package com.flab.modu.product.exception;

public class InsufficientStockException extends RuntimeException {

    private static final String MESSAGE= "상품의 재고가 부족합니다.";

    public InsufficientStockException() {
        super(MESSAGE);
    }

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientStockException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
