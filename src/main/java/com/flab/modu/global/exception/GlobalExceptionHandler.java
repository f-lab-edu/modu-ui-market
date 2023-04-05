package com.flab.modu.global.exception;

import com.flab.modu.global.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> errorHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<ErrorResponseDto>(
            new ErrorResponseDto(e.getAllErrors().get(0).getDefaultMessage()),
            HttpStatus.BAD_REQUEST);
    }
}
