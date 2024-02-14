package com.nayoon.purchase_service.controller;

import com.nayoon.purchase_service.controller.dto.response.PurchaseQuantityResponseDto;
import com.nayoon.purchase_service.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/purchases")
public class InternalPurchaseController {

  private final PurchaseService purchaseService;

  /**
   * 주문 상태 변경
   */
  @PostMapping
  public ResponseEntity<Void> updateStatus(
      @RequestParam(name = "id") Long purchaseId,
      @RequestParam(name = "status") String purchaseStatus
  ) {
    purchaseService.updateStatus(purchaseId, purchaseStatus);
    return ResponseEntity.ok().build();
  }

  /**
   * 주문 ID로 상품 ID 반환
   */
  @GetMapping
  public ResponseEntity<PurchaseQuantityResponseDto> findProductIdByPurchaseId(
      @RequestParam(name = "id") Long purchaseId
  ) {
    PurchaseQuantityResponseDto response = PurchaseQuantityResponseDto.dtoToResponseDto(
        purchaseService.findProductIdByPurchaseId(purchaseId));
    return ResponseEntity.ok().body(response);
  }

}
