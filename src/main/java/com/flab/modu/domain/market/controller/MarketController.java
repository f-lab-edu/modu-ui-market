package com.flab.modu.domain.market.controller;

import com.flab.modu.domain.market.domain.Market;
import com.flab.modu.domain.market.domain.MarketValidator;
import com.flab.modu.domain.market.exception.MarketDataBindingException;
import com.flab.modu.domain.market.repository.MarketRepository;
import com.flab.modu.domain.market.domain.MarketStatus;
import java.util.Arrays;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {

  private MarketValidator marketValidator;

  private MarketRepository marketRepository;


  public MarketController(MarketValidator marketValidator, MarketRepository marketRepository) {
    this.marketValidator = marketValidator;
    this.marketRepository = marketRepository;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(marketValidator);
  }

  @PostMapping("/market")
  public Market createMarket(@Valid @RequestBody Market market, BindingResult bindingResult)
      throws MarketDataBindingException {
    //TODO seller_id는 현재 파라메터로 받게 되어있으나, 사용자 인증 부분이 개발이 완료되면, 세션정보에서 가져오도록 수정이 필요하다.

    if (bindingResult.hasErrors()) {
      throw new MarketDataBindingException(bindingResult);
    }

    market.setStatus(MarketStatus.ACTIVE);
    return marketRepository.save(market);
  }
}
