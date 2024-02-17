package com.nayoon.purchase_service.controller.dto.response;

import com.nayoon.purchase_service.service.dto.PurchaseQuantityDto;
import lombok.Builder;

@Builder
public record PurchaseQuantityResponseDto(
    Long productId,
    Integer quantity,
    String productType
) {

  public static PurchaseQuantityResponseDto dtoToResponseDto(PurchaseQuantityDto dto) {
    return PurchaseQuantityResponseDto.builder()
        .productId(dto.productId())
        .quantity(dto.quantity())
        .productType(dto.productType().getProductType())
        .build();
  }

}
