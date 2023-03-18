package com.flab.modu.global.constants;

import com.flab.modu.global.response.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    public static final ResponseEntity<String> MARKET_NOT_FOUND =
        new ResponseEntity<String>(ResponseBuilder.getErrorJsonString("잘못된 마켓정보입니다."),
            HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<String> MARKET_NO_PERMISSION =
        new ResponseEntity<String>(ResponseBuilder.getErrorJsonString("잘못된 마켓정보입니다."),
            HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<String> WRONG_IMAGE_DATA =
        new ResponseEntity<String>(ResponseBuilder.getErrorJsonString("잘못된 이미지정보입니다."),
            HttpStatus.BAD_REQUEST);
}
