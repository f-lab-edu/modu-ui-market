package com.flab.modu.users.exception;

public class UnauthenticatedUserException extends RuntimeException {

    private static final String MESSAGE = "인증되지 않은 사용자입니다.";

    public UnauthenticatedUserException() {
        super(MESSAGE);
    }

    public UnauthenticatedUserException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
