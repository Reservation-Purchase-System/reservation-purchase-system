package com.nayoon.product_service.product.controller.dto.request;

import lombok.Builder;

@Builder
public record ProductUpdateRequestDto(
    String name,
    String content,
    Long price,
    Integer stock
) {

}
