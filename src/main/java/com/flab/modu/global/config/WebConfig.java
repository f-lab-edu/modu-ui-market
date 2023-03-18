package com.flab.modu.global.config;

import com.flab.modu.global.interceptor.LoginCheckInterceptor;
import com.flab.modu.product.converter.ProductStatusCodeRequestConverter;
import com.flab.modu.product.domain.common.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    private final ProductStatusCodeRequestConverter productStatusCodeRequestConverter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(productStatusCodeRequestConverter);
    }
}
