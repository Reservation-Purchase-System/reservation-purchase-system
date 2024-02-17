package com.nayoon.product_service.reservation_product.controller.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductUpdateRequestDto(
    String name,
    String content,
    Long price,
    Integer stock,
    LocalDateTime reservedAt
) {

}
