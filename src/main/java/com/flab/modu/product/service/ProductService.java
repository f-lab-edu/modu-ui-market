package com.flab.modu.product.service;

import com.flab.modu.market.domain.Market;
import com.flab.modu.market.exception.MarketNoPermissionException;
import com.flab.modu.market.exception.MarketNotFoundException;
import com.flab.modu.market.repository.MarketRepository;
import com.flab.modu.product.controller.ProductDto;
import com.flab.modu.product.controller.ProductDto.CreateRequest;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.exception.WrongImageDataException;
import com.flab.modu.product.repository.ProductRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProductService {

    private MarketRepository marketRepository;

    private ProductRepository productRepository;

    public ProductDto.CreateResponse createProduct(ProductDto.CreateRequest createRequest,
        MultipartFile imageMultipartFile, String loginId) {

        Market market = checkCreatingProductParameterValidation(createRequest, loginId);
        
        Product savedProduct = saveProduct(market, createRequest, imageMultipartFile);

        return ProductDto.CreateResponse.builder().product(savedProduct).build();
    }

    private Market checkCreatingProductParameterValidation(CreateRequest createRequest,
        String loginId) {
        Market market = marketRepository.findById(createRequest.getMarketId())
            .orElseThrow(MarketNotFoundException::new);

        if (!checkMyMarket(market, loginId)) {
            throw new MarketNoPermissionException();
        }
        return market;
    }

    private boolean checkMyMarket(Market market, String loginId) {
        if (!market.getSellerId().equals(loginId)) {
            return false;
        }

        return true;
    }

    private Product saveProduct(Market myMarket, CreateRequest createRequest,
        MultipartFile imageMultipartFile) {
        Product product = createRequest.toEntity(myMarket, getImageBinary(imageMultipartFile));
        return productRepository.save(product);
    }

    private byte[] getImageBinary(MultipartFile imageMultipartFile) {
        try {
            if (imageMultipartFile == null) {
                return null;
            } else {
                return imageMultipartFile.getBytes();
            }
        } catch (IOException e) {
            throw new WrongImageDataException();
        }
    }
}
