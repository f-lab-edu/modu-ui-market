package com.flab.modu.market.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicatedUrlException extends ResponseStatusException {

    public DuplicatedUrlException() {
        super(HttpStatus.BAD_REQUEST, "중복된 마켓주소입니다.");
    }
}
