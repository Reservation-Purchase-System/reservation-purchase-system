package com.nayoon.product_service.reservation_product.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductUpdateRequest(
    String name,
    String content,
    Long price,
    Integer stock,
    LocalDateTime reservedAt
) {

}
