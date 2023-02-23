package com.flab.modu.market.controller;

import com.flab.modu.market.domain.Market;
import com.flab.modu.market.service.MarketService;
import com.flab.modu.users.domain.common.UserConstant;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequiredArgsConstructor
@RestController
public class MarketController {

    private final MarketService marketService;

    @PostMapping("/markets")
    public MarketDto.CreateResponse createMarket(
        @RequestBody @Valid MarketDto.CreateRequest createRequest,
        @SessionAttribute(value = UserConstant.EMAIL) String sellerId) {
        return marketService.createMarket(createRequest, sellerId);
    }
}
