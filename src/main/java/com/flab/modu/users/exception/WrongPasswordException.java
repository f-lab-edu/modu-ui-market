package com.flab.modu.users.exception;

public class WrongPasswordException extends RuntimeException {

    private static final String MESSAGE = "잘못된 비밀번호입니다.";

    public WrongPasswordException() {
        super(MESSAGE);
    }

    public WrongPasswordException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
