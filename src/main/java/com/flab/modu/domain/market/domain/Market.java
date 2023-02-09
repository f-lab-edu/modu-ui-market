package com.flab.modu.domain.market.domain;

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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    @NotNull
    private String sellerId;

    @Setter
    @Column(nullable = false, length = 200)
    @NotNull
    private String name;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketStatus status;

    @Setter
    @Column(nullable = false, length = 100)
    @NotNull
    private String url;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public Market() {
    }

    public Market(String sellerId, String name, String url) {
        this.sellerId = sellerId;
        this.name = name;
        this.url = url;
    }

    public static Market of(String sellerId, String name, String url) {
        return new Market(sellerId, name, url);
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
