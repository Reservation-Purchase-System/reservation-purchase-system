package com.nayoon.product_service.reservation_product.controller.dto.response;

import com.nayoon.product_service.reservation_product.service.dto.ReservationProductStockDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductStockResponseDto(
    Long productId,
    Integer stock,
    LocalDateTime reservedAt
) {

  public static ReservationProductStockResponseDto dtoToResponseDto(ReservationProductStockDto dto) {
    return ReservationProductStockResponseDto.builder()
        .productId(dto.productId())
        .stock(dto.stock())
        .reservedAt(dto.reservedAt())
        .build();
  }

}
