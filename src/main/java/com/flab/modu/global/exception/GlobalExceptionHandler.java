package com.flab.modu.global.exception;

import com.flab.modu.market.exception.DuplicatedUrlException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> errorHandler(MethodArgumentNotValidException e) {

        return ResponseEntity.badRequest().body(
            "{\"message\":\"" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                + "\"}"
        );
    }

    @ExceptionHandler({DuplicatedUrlException.class})
    public ResponseEntity<String> handleDuplicatedUrlException(DuplicatedUrlException e) {

        return ResponseEntity.badRequest().body(
            "{\"message\":\"중복된 마켓주소입니다.\"}"
        );
    }
}
