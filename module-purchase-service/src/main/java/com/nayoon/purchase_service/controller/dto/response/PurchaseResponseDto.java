package com.nayoon.purchase_service.controller.dto.response;

import com.nayoon.purchase_service.entity.Purchase;
import java.time.LocalDateTime;

public record PurchaseResponseDto(
    Long orderId,
    Long productId,
    Integer quantity,
    Long price,
    String address,
    LocalDateTime createdAt
) {

  public static PurchaseResponseDto entityToDto(Purchase purchase) {
    return new PurchaseResponseDto(
        purchase.getId(),
        purchase.getProductId(),
        purchase.getQuantity(),
        purchase.getPrice(),
        purchase.getAddress(),
        purchase.getCreatedAt()
    );
  }

}
