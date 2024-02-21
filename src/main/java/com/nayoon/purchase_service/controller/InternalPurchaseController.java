package com.nayoon.purchase_service.controller;

import com.nayoon.purchase_service.controller.dto.response.PurchaseQuantityResponseDto;
import com.nayoon.purchase_service.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/purchases")
public class InternalPurchaseController {

  private final PurchaseService purchaseService;

  /**
   * 주문 ID로 상품 ID 반환
   */
  @GetMapping("/{purchaseId}")
  public ResponseEntity<PurchaseQuantityResponseDto> findProductIdByPurchaseId(
      @PathVariable(name = "purchaseId") Long purchaseId
  ) {
    PurchaseQuantityResponseDto response = PurchaseQuantityResponseDto.dtoToResponseDto(
        purchaseService.findProductIdByPurchaseId(purchaseId));
    return ResponseEntity.ok().body(response);
  }

  /**
   * 주문 취소
   */
  @DeleteMapping
  public ResponseEntity<Void> cancel(
      @RequestParam(name = "id") Long purchaseId
  ) {
    purchaseService.cancel(purchaseId);
    return ResponseEntity.ok().build();
  }

  /**
   * 결제 프로세스 진입한 모든 주문 반환
   */
  @GetMapping("/{productId}/quantity")
  public ResponseEntity<Integer> getQuantitySumByProductId(
      @PathVariable(name = "productId") Long productId
  ) {
    return ResponseEntity.ok().body(purchaseService.getQuantitySumByProductId(productId));
  }

}
