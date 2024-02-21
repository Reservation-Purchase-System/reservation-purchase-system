package com.nayoon.purchase_service.controller.dto.response;

public record PurchaseResponseDto(
    Long orderId,
    Long productId,
    Integer quantity,
    String address
) {

}
