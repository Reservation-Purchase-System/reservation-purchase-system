package com.nayoon.purchase_service.client.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductStockDto(
    Long productId,
    Integer stock,
    LocalDateTime reservedAt
) {

}
