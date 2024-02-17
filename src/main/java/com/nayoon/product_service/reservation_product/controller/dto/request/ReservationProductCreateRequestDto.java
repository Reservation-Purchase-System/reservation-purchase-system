package com.nayoon.product_service.reservation_product.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductCreateRequestDto(
    @NotBlank
    String name,
    @NotBlank
    String content,
    @NotNull
    Long price,
    @NotNull
    Integer stock,
    @NotNull
    LocalDateTime reservedAt
) {

}
