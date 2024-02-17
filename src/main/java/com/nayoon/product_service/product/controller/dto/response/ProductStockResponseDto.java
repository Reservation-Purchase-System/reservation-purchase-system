package com.nayoon.product_service.product.controller.dto.response;

import com.nayoon.product_service.product.service.dto.ProductStockDto;
import lombok.Builder;

@Builder
public record ProductStockResponseDto(
    Long productId,
    Integer stock
) {

  public static ProductStockResponseDto dtoToResponseDto(ProductStockDto productStockDto) {
    return ProductStockResponseDto.builder()
        .productId(productStockDto.productId())
        .stock(productStockDto.stock())
        .build();
  }

}
