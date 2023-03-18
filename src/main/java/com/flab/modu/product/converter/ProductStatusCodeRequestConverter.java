package com.flab.modu.product.converter;

import com.flab.modu.product.domain.common.ProductStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductStatusCodeRequestConverter implements Converter<String, ProductStatus> {

    @Override
    public ProductStatus convert(String code) {
        return ProductStatus.of(code);
    }
}
