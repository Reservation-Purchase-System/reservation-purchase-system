package com.nayoon.product_service.reservation_product.service.dto;

import com.nayoon.product_service.reservation_product.entity.ReservationProductStock;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductStockDto(
    Long productId,
    Integer stock,
    LocalDateTime reservedAt
) {

  public static ReservationProductStockDto toDto(ReservationProductStock productStock, LocalDateTime reservedAt) {
    return ReservationProductStockDto.builder()
        .productId(productStock.getProductId())
        .stock(productStock.getStock())
        .reservedAt(reservedAt)
        .build();
  }

}
