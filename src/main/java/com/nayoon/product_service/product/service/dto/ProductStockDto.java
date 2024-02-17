package com.nayoon.product_service.product.service.dto;

import com.nayoon.product_service.product.entity.ProductStock;
import lombok.Builder;

@Builder
public record ProductStockDto(
    Long productId,
    Integer stock
) {

  public static ProductStockDto toDto(ProductStock productStock) {
    return ProductStockDto.builder()
        .productId(productStock.getProductId())
        .stock(productStock.getStock())
        .build();
  }

}
