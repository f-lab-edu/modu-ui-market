package com.flab.modu.product.service;

import com.flab.modu.market.domain.Market;
import com.flab.modu.market.exception.MarketNoPermissionException;
import com.flab.modu.market.exception.MarketNotFoundException;
import com.flab.modu.market.repository.MarketRepository;
import com.flab.modu.product.controller.ProductDto;
import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.exception.WrongImageDataException;
import com.flab.modu.product.repository.ProductRepository;
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
        //유효성 검사
        Market market = marketRepository.findById(createRequest.getMarketId())
            .orElseThrow(MarketNotFoundException::new);

        if (!checkMyMarket(market, loginId)) {
            throw new MarketNoPermissionException();
        }

        //DTO -> entity로 변환
        Product product = createRequest.toEntity(market, getImageBinary(imageMultipartFile));

        //entity 저장
        Product savedProduct = productRepository.save(product);

        //응답값 만들어 반환
        return ProductDto.CreateResponse.builder().product(savedProduct).build();
    }

    private byte[] getImageBinary(MultipartFile imageMultipartFile) {
        try {
            return imageMultipartFile.getBytes();
        } catch (Exception e) {
            throw new WrongImageDataException();
        }
    }

    private boolean checkMyMarket(Market market, String loginId) {
        if (!market.getSellerId().equals(loginId)) {
            return false;
        }

        return true;
    }
}
