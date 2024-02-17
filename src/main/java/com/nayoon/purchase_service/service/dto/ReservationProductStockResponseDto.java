package com.nayoon.purchase_service.service.dto;

import com.nayoon.purchase_service.client.dto.ReservationProductStockDto;
import java.time.LocalDateTime;

public record ReservationProductStockResponseDto(
    Integer stock,
    LocalDateTime reservedAt
) {

  public static ReservationProductStockResponseDto toResponseDto(ReservationProductStockDto dto) {
    return new ReservationProductStockResponseDto(
        dto.stock(),
        dto.reservedAt()
    );
  }

}
