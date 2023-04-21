package com.flab.modu.market.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MarketNotFoundException extends ResponseStatusException {

    public MarketNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 마켓정보입니다.");
    }
}
