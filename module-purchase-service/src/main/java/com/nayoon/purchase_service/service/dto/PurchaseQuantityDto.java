package com.nayoon.purchase_service.service.dto;

import com.nayoon.purchase_service.entity.Purchase;
import lombok.Builder;

@Builder
public record PurchaseQuantityDto(
    Long productId,
    Integer quantity
) {

  public static PurchaseQuantityDto entityToDto(Purchase purchase) {
    return PurchaseQuantityDto.builder()
        .productId(purchase.getProductId())
        .quantity(purchase.getQuantity())
        .build();
  }

}
