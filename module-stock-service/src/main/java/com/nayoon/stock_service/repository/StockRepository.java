package com.nayoon.stock_service.repository;

import com.nayoon.stock_service.entity.Stock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

  Optional<Stock> findByProductId(Long productId);

  boolean existsByProductId(Long productId);

  void deleteByProductId(Long productId);

}
