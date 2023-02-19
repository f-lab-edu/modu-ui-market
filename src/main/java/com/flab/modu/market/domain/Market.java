package com.flab.modu.market.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String sellerId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketStatus status;

    @Column(nullable = false, length = 100)
    private String url;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Builder
    public Market(String sellerId, String name, String url, MarketStatus status) {
        this.sellerId = sellerId;
        this.name = name;
        this.url = url;
        this.status = status;
    }

    public void updateMarket(String name, String url, MarketStatus status){
        this.name = name;
        this.url = url;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Market)) {
            return false;
        }
        Market market = (Market) o;
        return id.equals(market.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
