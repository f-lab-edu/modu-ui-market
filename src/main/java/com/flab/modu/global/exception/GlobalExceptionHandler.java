package com.flab.modu.global.exception;

import com.flab.modu.global.response.ErrorResponseDto;
import com.flab.modu.order.exception.OrderFailureException;
import com.flab.modu.product.exception.InsufficientStockException;
import com.flab.modu.product.exception.NotExistProductException;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.exception.UnauthenticatedUserException;
import com.flab.modu.users.exception.WrongPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler(WrongPasswordException.class)
    public final ResponseEntity<String> handleWrongPasswordException(
        WrongPasswordException exception) {
        log.debug(exception.getMessage(), exception);

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> errorHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<ErrorResponseDto>(
            new ErrorResponseDto(e.getAllErrors().get(0).getDefaultMessage()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public final ResponseStatusException handleInsufficientStockException(
        InsufficientStockException exception) {
        log.debug(exception.getMessage(), exception);

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(OrderFailureException.class)
    public final ResponseStatusException handleOrderFailureException(
        OrderFailureException exception) {
        log.debug(exception.getMessage(), exception);

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotExistProductException.class)
    public final ResponseStatusException handleNotExistProductException(
        NotExistProductException exception) {
        log.debug(exception.getMessage(), exception);

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
