package com.nayoon.stock_service.service;

import com.nayoon.stock_service.client.PurchaseClient;
import com.nayoon.stock_service.common.exception.CustomException;
import com.nayoon.stock_service.common.exception.ErrorCode;
import com.nayoon.stock_service.entity.Stock;
import com.nayoon.stock_service.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

  private final StockRepository stockRepository;
  private final PurchaseClient purchaseClient;

  @Transactional
  @CachePut(value = "stock", key = "#productId")
  public Integer createOrUpdate(Long productId, Integer newStock) {
    boolean exists = stockRepository.existsByProductId(productId);

    if (exists) {
      return update(productId, newStock);
    } else {
      Stock stock = Stock.builder()
          .productId(productId)
          .stock(newStock)
          .initialStock(newStock)
          .build();
      stockRepository.save(stock);
      return stock.getStock();
    }
  }

  private Integer update(Long productId, Integer newStock) {
    Stock stock = loadStock(productId);

    stock.updateInitialStock(newStock);
    stock.updateStock(calculateStock(productId, newStock));

    stockRepository.save(stock);
    return stock.getStock();
  }

  @Transactional
  @CachePut(value = "stock", key = "#productId")
  public Integer increaseStock(Long productId, Integer quantity) {
    Stock stock = loadStock(productId);
    stock.increase(quantity);

    if (stock.getInitialStock() < stock.getStock()) {
      throw new CustomException(ErrorCode.LIMIT_OF_STOCK);
    }

    stockRepository.save(stock);
    return stock.getStock();
  }

  @Transactional
  @CachePut(value = "stock", key = "#productId")
  public Integer decreaseStock(Long productId, Integer quantity) {
    Stock stock = loadStock(productId);

    if (stock.getStock() < quantity) {
      throw new CustomException(ErrorCode.OUT_OF_STOCK);
    }

    stock.decrease(quantity);

    stockRepository.save(stock);
    return stock.getStock();
  }

  private Stock loadStock(Long productId) {
    return stockRepository.findByProductId(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.STOCK_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  @Cacheable(value = "stock", key = "#productId")
  public Integer getStock(Long productId) {
    Stock stock = loadStock(productId);
    return stock.getStock();
  }

  private Integer calculateStock(Long productId, Integer newStock) {
    // purchase_service에 결제 프로세스 진입한 주문들의 quantity 합 요청
    Integer quantitySum = purchaseClient.getQuantitySumByProductId(productId);

    if (newStock < quantitySum) {
      throw new CustomException(ErrorCode.INVALID_NEW_STOCK);
    }

    return newStock - quantitySum;
  }

}
