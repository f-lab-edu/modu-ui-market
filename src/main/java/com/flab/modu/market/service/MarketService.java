package com.flab.modu.market.service;

import com.flab.modu.market.controller.MarketDto;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.exception.DuplicatedUrlException;
import com.flab.modu.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public MarketDto.CreateResponse createMarket(MarketDto.CreateRequest createMarketRequest,
        String sellerId) {
        if (checkUrlDuplicate(createMarketRequest.getUrl())) {
            throw new DuplicatedUrlException();
        }

        Market savedMarket = marketRepository.save(createMarketRequest.toEntity(sellerId));
        return MarketDto.CreateResponse.builder().market(savedMarket).build();
    }

    private boolean checkUrlDuplicate(String url) {
        return marketRepository.existsByUrl(url);
    }
}
