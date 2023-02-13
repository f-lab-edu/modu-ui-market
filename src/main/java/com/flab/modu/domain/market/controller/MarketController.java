package com.flab.modu.domain.market.controller;

import com.flab.modu.domain.market.domain.Market;
import com.flab.modu.domain.market.exception.MarketDataBindingException;
import com.flab.modu.domain.market.service.MarketService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MarketController {

    private final MarketService marketService;

    @PostMapping("/markets")
    public MarketDto.CreateResponse createMarket(
        @RequestBody @Valid MarketDto.CreateRequest createMarketRequest,
        BindingResult bindingResult) throws MarketDataBindingException {
        //TODO seller_id는 현재 파라메터로 받게 되어있으나, 사용자 인증 부분이 개발이 완료되면, 세션정보에서 가져오도록 수정이 필요하다.

        if (bindingResult.hasErrors()) {
            throw new MarketDataBindingException(bindingResult);
        }

        Market market = marketService.createMarket(createMarketRequest);

        return MarketDto.CreateResponse.builder().market(market).build();
    }
}
