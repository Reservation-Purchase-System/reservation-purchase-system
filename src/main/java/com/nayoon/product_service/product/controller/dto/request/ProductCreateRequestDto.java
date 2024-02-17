package com.nayoon.product_service.product.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductCreateRequestDto(
    @NotBlank
    String name,
    @NotBlank
    String content,
    @NotNull
    Long price,
    @NotNull
    Integer stock
) {

}
