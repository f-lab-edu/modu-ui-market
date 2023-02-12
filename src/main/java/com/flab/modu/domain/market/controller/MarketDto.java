package com.flab.modu.domain.market.controller;

import com.flab.modu.domain.market.domain.Market;
import com.flab.modu.domain.market.domain.MarketStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class MarketDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequest {

        private String sellerId;

        @NotBlank
        @Length(min = 1, max = 200)
        private String name;

        @NotBlank
        @Length(min = 1, max = 100)
        @Pattern(regexp = "^[a-zA-Z]*$", message = "주소는 영어대소문자만 입력할 수 있습니다.")
        private String url;

        @Builder
        public CreateRequest(String sellerId, String name, String url) {
            this.sellerId = sellerId;
            this.name = name;
            this.url = url;
        }

        public Market toEntity() {
            return Market.builder()
                .sellerId(sellerId)
                .name(name)
                .url(url)
                .status(MarketStatus.ACTIVE)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CreateResponse {

        private Long id;
        private String sellerId;
        private String name;
        private String url;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        @Builder
        public CreateResponse(Market market) {
            this.id = market.getId();
            this.sellerId = market.getSellerId();
            this.name = market.getName();
            this.url = market.getUrl();
            this.createdAt = market.getCreatedAt();
            this.modifiedAt = market.getModifiedAt();
        }
    }
}
