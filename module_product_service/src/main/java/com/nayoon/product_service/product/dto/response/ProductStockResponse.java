package com.nayoon.product_service.product.dto.response;

import com.nayoon.product_service.product.entity.ProductStock;
import lombok.Builder;

@Builder
public record ProductStockResponse(
    Long productId,
    Integer stock
) {

  public static ProductStockResponse from(ProductStock productStock) {
    return ProductStockResponse.builder()
        .productId(productStock.getProductId())
        .stock(productStock.getStock())
        .build();
  }

}
