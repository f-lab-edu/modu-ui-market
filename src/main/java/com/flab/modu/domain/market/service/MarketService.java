package com.flab.modu.domain.market.service;

import com.flab.modu.domain.market.domain.Market;
import com.flab.modu.domain.market.domain.MarketStatus;
import com.flab.modu.domain.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public Market createMarket(Market market){
        market.setStatus(MarketStatus.ACTIVE);
        return marketRepository.save(market);
    }
}
