package com.flab.modu.global.constants;

import com.flab.modu.global.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    public static final ResponseEntity<Object> MARKET_NOT_FOUND =
        new ResponseEntity<Object>(new ErrorResponseDto("잘못된 마켓정보입니다."),
            HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<Object> MARKET_NO_PERMISSION =
        new ResponseEntity<Object>(new ErrorResponseDto("잘못된 마켓정보입니다."),
            HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<Object> WRONG_IMAGE_DATA =
        new ResponseEntity<Object>(new ErrorResponseDto("잘못된 이미지정보입니다."),
            HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<Object> MARKET_URL_DUPLATED =
        new ResponseEntity<Object>(new ErrorResponseDto("중복된 마켓주소입니다."),
            HttpStatus.BAD_REQUEST);

    public static ResponseEntity<Object> buildResponseEntity(String message) {
        return new ResponseEntity<Object>(new ErrorResponseDto(message),
            HttpStatus.BAD_REQUEST);
    }
}
