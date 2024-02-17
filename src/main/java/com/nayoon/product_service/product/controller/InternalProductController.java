package com.nayoon.product_service.product.controller;

import com.nayoon.product_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/products")
public class InternalProductController {

  private final ProductService productService;

  /**
   * 주문가능한 상품 재고 조회
   */
  @GetMapping("/stock")
  public ResponseEntity<Integer> getProductStockById(
      @RequestParam(name = "id") Long productId
  ) {
    return ResponseEntity.ok().body(productService.getProductStockById(productId).stock());
  }

  /**
   * 상품 재고 증가
   */
  @PostMapping("/stock/add")
  public ResponseEntity<Void> addProductStock(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "quantity") Integer quantity
  ) {
    productService.addProductStock(productId, quantity);
    return ResponseEntity.ok().build();
  }

  /**
   * 상품 재고 감소
   */
  @PostMapping("/stock/subtract")
  public ResponseEntity<Void> subtractProductStock(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "quantity") Integer quantity
  ) {
    productService.subtractProductStock(productId, quantity);
    return ResponseEntity.ok().build();
  }

}
