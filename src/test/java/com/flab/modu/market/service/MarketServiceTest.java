package com.flab.modu.market.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.market.controller.MarketDto;
import com.flab.modu.market.controller.MarketDto.CreateRequest;
import com.flab.modu.market.domain.Market;
import com.flab.modu.market.exception.DuplicatedUrlException;
import com.flab.modu.market.repository.MarketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketServiceTest {

    @Mock
    private MarketRepository marketRepository;

    @InjectMocks
    private MarketService marketService;

    @Test
    @DisplayName("정상적으로 마켓생성에 성공한다.")
    public void givenTestData_whenCreatingMarket_thenSuccess() {
        // given
        MarketDto.CreateRequest createRequest = createMarketDto();
        given(marketRepository.save(any(Market.class))).willReturn(createRequest.toEntity());

        // when
        marketService.createMarket(createRequest);

        // then
        then(marketRepository).should().existsByUrl(anyString());
        then(marketRepository).should().save(any(Market.class));
    }

    @Test
    @DisplayName("마켓주소 중복으로 마켓생성에 실패한다.")
    public void givenDuplicatedUrl_whenCreatingMarket_thenFailure() {
        // given
        MarketDto.CreateRequest createRequest = createMarketDto();
        String existingUrl = "MarketUrl";
        given(marketRepository.existsByUrl(existingUrl)).willReturn(true);

        // when
        assertThrows(DuplicatedUrlException.class, () -> marketService.createMarket(createRequest));

        // then
        then(marketRepository).should().existsByUrl(existingUrl);
        then(marketRepository).shouldHaveNoMoreInteractions();
    }

    private CreateRequest createMarketDto() {
        return MarketDto.CreateRequest.builder()
            .sellerId("sellerId")
            .url("MarketUrl")
            .name("마켓명")
            .build();
    }
}