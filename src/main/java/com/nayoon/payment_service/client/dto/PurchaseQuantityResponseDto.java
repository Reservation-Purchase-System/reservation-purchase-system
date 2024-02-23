package com.nayoon.payment_service.client.dto;

import lombok.Builder;

@Builder
public record PurchaseQuantityResponseDto(
    Long productId,
    Integer quantity,
    String productType
) {

}
