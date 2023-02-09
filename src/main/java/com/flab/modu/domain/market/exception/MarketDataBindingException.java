package com.flab.modu.domain.market.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

public class MarketDataBindingException extends Exception {

    @Getter
    private BindingResult bindingResult;

    public MarketDataBindingException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
