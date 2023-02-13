package com.flab.modu.market.service;

import com.flab.modu.market.controller.MarketDto;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public Market createMarket(MarketDto.CreateRequest createMarketRequest) {
        Market market = createMarketRequest.toEntity();
        return marketRepository.save(market);
    }
}
