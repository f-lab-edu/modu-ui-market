package com.flab.modu.market.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.modu.global.config.JpaConfig;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.domain.MarketStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("Market JPA 연결 테스트")
@ActiveProfiles("test")
@Import(value = JpaConfig.class)
@DataJpaTest
public class MarketRepositoryTest {

    @Autowired
    private MarketRepository marketRepository;

    private Market insertedMarket;

    @BeforeEach
    private void beforeEach() {
        insertedMarket = insertMarketData("sellerId", "MarketUrl", "마켓명", MarketStatus.ACTIVE);
    }

    @Test
    @DisplayName("select 테스트")
    void givenTestData_whenSelecting_thenWorksFine() throws Exception {
        //given

        //when
        List<Market> markets = marketRepository.findAll();

        //then
        assertThat(markets)
            .isNotNull()
            .hasSize(1);
    }

    @Test
    @DisplayName("insert 테스트")
    void givenTestData_whenInserting_thenWorksFine() throws Exception {
        //given
        long prevCount = marketRepository.count();

        //when
        Market savedMarket = marketRepository.save(
            new Market("sellerId", "판매자명", "MarketUrl", MarketStatus.ACTIVE));

        //then
        assertThat(marketRepository.count()).isEqualTo(prevCount + 1);
    }

    @Test
    @DisplayName("update 테스트")
    void givenTestData_whenUpdating_thenWorksFine() throws Exception {
        //given
        Market market = marketRepository.findById(insertedMarket.getId()).orElseThrow();
        String updateUrl = "updatedUrl";
        market.updateMarket(market.getName(), updateUrl, market.getStatus());

        //when
        Market savedMarket = marketRepository.saveAndFlush(market);

        //then
        assertThat(savedMarket).hasFieldOrPropertyWithValue("url", updateUrl);
    }

    @Test
    @DisplayName("delete 테스트")
    void givenTestData_whenDeleting_thenWorksFine() throws Exception {
        //given
        Market market = marketRepository.findById(insertedMarket.getId()).orElseThrow();
        long count = marketRepository.count();

        // when
        marketRepository.delete(market);

        //then
        assertThat(marketRepository.count()).isEqualTo(count - 1);
    }

    private Market insertMarketData(String sellerId, String url, String name, MarketStatus status) {
        Market market = Market.builder()
            .sellerId(sellerId)
            .url(url)
            .name(name)
            .status(status)
            .build();

        return marketRepository.save(market);
    }
}
