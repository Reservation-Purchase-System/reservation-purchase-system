package com.nayoon.purchase_service.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PurchaseCreateRequestDto(
    @NotNull
    Long productId,
    @NotNull
    Integer quantity,
    @NotEmpty
    String address
) {

}
