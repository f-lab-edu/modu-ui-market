package com.flab.modu.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongImageDataException extends ResponseStatusException {

    public WrongImageDataException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 이미지정보입니다.");
    }
}
