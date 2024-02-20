package com.nayoon.stock_service.controller;

import com.nayoon.stock_service.controller.dto.StockResponseDto;
import com.nayoon.stock_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
public class StockController {

  private final StockService stockService;

  /**
   * 상품 재고 조회
   */
  @GetMapping("/{productId}")
  public ResponseEntity<StockResponseDto> getStock(
      @PathVariable(name = "productId") Long productId
  ) {
    StockResponseDto response = StockResponseDto.valueToDto(stockService.getStock(productId));
    return ResponseEntity.ok().body(response);
  }

}
