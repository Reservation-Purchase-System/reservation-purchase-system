package com.nayoon.stock_service.controller;

import com.nayoon.stock_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/stocks")
public class InternalStockController {

  private final StockService stockService;

  /**
   * 상품 재고 생성 및 업데이트
   */
  @PostMapping
  public ResponseEntity<Void> createOrUpdate(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "stock") Integer stock
  ) {
    stockService.createOrUpdate(productId, stock);
    return ResponseEntity.ok().build();
  }

  /**
   * 상품 재고 증가
   */
  @PostMapping("/increase")
  public ResponseEntity<Void> increaseProductStock(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "quantity") Integer quantity
  ) {
    stockService.increaseStock(productId, quantity);
    return ResponseEntity.ok().build();
  }

  /**
   * 상품 재고 감소
   */
  @PostMapping("/decrease")
  public ResponseEntity<Void> decreaseProductStock(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "quantity") Integer quantity
  ) {
    stockService.decreaseStock(productId, quantity);
    return ResponseEntity.ok().build();
  }

  /**
   * 상품 재고 조회
   */
  @GetMapping("/{productId}")
  public ResponseEntity<Integer> findStock(
      @PathVariable(name = "productId") Long productId
  ) {
    return ResponseEntity.ok().body(stockService.getStock(productId));
  }

}
