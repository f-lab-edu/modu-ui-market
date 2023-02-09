package com.flab.modu.global.advice;

import com.flab.modu.domain.market.exception.MarketDataBindingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MarketDataBindingException.class)
    public ResponseEntity errorHandler(MarketDataBindingException e) {

        return ResponseEntity.badRequest().body(
            "{\"message\":\"" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage() + "\"}"
        );
    }
}
