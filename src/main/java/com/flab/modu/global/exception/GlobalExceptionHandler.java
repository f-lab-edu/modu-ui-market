package com.flab.modu.global.exception;

import com.flab.modu.global.constants.ApiResponse;
import com.flab.modu.market.exception.DuplicatedUrlException;
import com.flab.modu.market.exception.MarketNoPermissionException;
import com.flab.modu.market.exception.MarketNotFoundException;
import com.flab.modu.product.exception.WrongImageDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> errorHandler(MethodArgumentNotValidException e) {
        return ApiResponse.buildResponseEntity(e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler({DuplicatedUrlException.class})
    public ResponseEntity<Object> handleDuplicatedUrlException(DuplicatedUrlException e) {
        return ApiResponse.MARKET_URL_DUPLATED;
    }

    @ExceptionHandler({MarketNotFoundException.class})
    public ResponseEntity<Object> handleMarketNotFoundException(MarketNotFoundException e) {
        return ApiResponse.MARKET_NOT_FOUND;
    }

    @ExceptionHandler({MarketNoPermissionException.class})
    public ResponseEntity<Object> handleMarketNoPermissionException(MarketNoPermissionException e) {
        return ApiResponse.MARKET_NO_PERMISSION;
    }

    @ExceptionHandler({WrongImageDataException.class})
    public ResponseEntity<Object> handleWrongImageDataException(WrongImageDataException e) {
        return ApiResponse.WRONG_IMAGE_DATA;
    }
}
