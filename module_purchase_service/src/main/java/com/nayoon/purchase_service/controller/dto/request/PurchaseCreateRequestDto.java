package com.nayoon.purchase_service.controller.dto.request;

import lombok.Builder;

@Builder
public record PurchaseCreateRequestDto(
    Long productId,
    Integer quantity,
    String productType,
    String address
) {

}
