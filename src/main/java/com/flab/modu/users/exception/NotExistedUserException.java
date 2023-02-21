package com.flab.modu.users.exception;

public class NotExistedUserException
    extends IllegalArgumentException {

    private static final String MESSAGE = "아이디나 패스워드를 확인해주세요.";

    public NotExistedUserException() {
        super(MESSAGE);
    }

    public NotExistedUserException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
