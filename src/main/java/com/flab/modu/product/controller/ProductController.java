package com.flab.modu.product.controller;

import com.flab.modu.product.service.ProductService;
import com.flab.modu.users.domain.common.UserConstant;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ProductDto.CreateResponse createProduct(
        @RequestPart("product") @Valid ProductDto.CreateRequest createRequest,
        @RequestPart(value = "image",required = false) MultipartFile imageMultipartFile,
        @SessionAttribute(value = UserConstant.EMAIL) String sellerId) {
        return productService.createProduct(createRequest, imageMultipartFile, sellerId);
    }
}
