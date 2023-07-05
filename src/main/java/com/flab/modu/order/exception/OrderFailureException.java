package com.flab.modu.order.exception;

public class OrderFailureException extends RuntimeException {

    private static final String MESSAGE= "상품 주문에 실패하였습니다.";

    public OrderFailureException(String message) {
        super(message);
    }

    public OrderFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderFailureException() {
        super(MESSAGE);
    }

    public OrderFailureException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
