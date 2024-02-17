package com.nayoon.purchase_service.controller.dto.response;

import com.nayoon.purchase_service.type.PurchaseStatus;

public record PurchaseResponseDto(
    Long orderId,
    Long productId,
    Integer quantity,
    String address,
    PurchaseStatus purchaseStatus
) {

}
