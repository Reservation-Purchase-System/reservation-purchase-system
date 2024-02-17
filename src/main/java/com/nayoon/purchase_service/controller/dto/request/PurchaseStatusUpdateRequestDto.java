package com.nayoon.purchase_service.controller.dto.request;

import lombok.Builder;

@Builder
public record PurchaseStatusUpdateRequestDto(
    Long purchaseId,
    String purchaseStatus
) {

}
