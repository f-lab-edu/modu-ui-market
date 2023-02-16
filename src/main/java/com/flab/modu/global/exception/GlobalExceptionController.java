package com.flab.modu.global.exception;

import com.flab.modu.users.exception.DuplicatedEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(DuplicatedEmailException.class)
    public final ResponseEntity<String> handleDuplicatedEmailException(
        DuplicatedEmailException exception) {
        log.debug("중복된 이메일입니다.", exception);

        return new ResponseEntity<>("이미 가입된 이메일입니다.", HttpStatus.CONFLICT);
    }
}
