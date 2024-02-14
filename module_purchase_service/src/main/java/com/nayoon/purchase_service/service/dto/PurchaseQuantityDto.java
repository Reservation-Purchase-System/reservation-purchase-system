package com.nayoon.purchase_service.service.dto;

import com.nayoon.purchase_service.entity.Purchase;
import com.nayoon.purchase_service.type.ProductType;
import lombok.Builder;

@Builder
public record PurchaseQuantityDto(
    Long productId,
    Integer quantity,
    ProductType productType
) {

  public static PurchaseQuantityDto entityToDto(Purchase purchase) {
    return PurchaseQuantityDto.builder()
        .productId(purchase.getProductId())
        .quantity(purchase.getQuantity())
        .productType(purchase.getProductType())
        .build();
  }

}
