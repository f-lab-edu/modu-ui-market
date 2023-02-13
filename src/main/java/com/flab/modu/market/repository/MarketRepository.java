package com.flab.modu.market.repository;

import com.flab.modu.market.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {

}
