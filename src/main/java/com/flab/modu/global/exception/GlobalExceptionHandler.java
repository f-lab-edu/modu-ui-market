package com.flab.modu.global.exception;

import com.flab.modu.market.exception.DuplicatedUrlException;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.exception.UnauthenticatedUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public final ResponseEntity<String> handleDuplicatedEmailException(
        DuplicatedEmailException exception) {
        log.debug("중복된 이메일입니다.", exception);

        return new ResponseEntity<>("이미 가입된 이메일입니다.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotExistedUserException.class)
    public final ResponseEntity<String> handleNotExistedUserException(
        NotExistedUserException exception) {
        log.debug("해당 이메일, 패스워드에 일치하는 사용자가 없습니다.", exception);

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    public final ResponseEntity<String> handleUnauthenticatedUserException(
        UnauthenticatedUserException exception) {
        log.debug("인증되지 않은 사용자입니다.", exception);

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

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
