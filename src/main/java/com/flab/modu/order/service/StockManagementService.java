package com.flab.modu.order.service;

import com.flab.modu.product.domain.entity.Product;
import com.flab.modu.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockManagementService {

    private final ProductService productService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Product subtractStock(Long productId, int amount) {
        Product product = productService.getProduct(productId);

        productService.sellProduct(product, amount);

        return product;
    }
}
