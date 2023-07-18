package com.flab.modu.product.repository;

import com.flab.modu.product.domain.entity.Product;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @Override
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Product> findById(Long aLong);
}
