package com.nayoon.purchase_service.mapper;

import com.nayoon.purchase_service.controller.dto.response.PurchaseResponseDto;
import com.nayoon.purchase_service.entity.Purchase;
import org.springframework.stereotype.Service;

@Service
public class PurchaseResponseMapper {

  public static PurchaseResponseDto toDto(Purchase purchase) {
    return new PurchaseResponseDto(
        purchase.getId(),
        purchase.getProductId(),
        purchase.getQuantity(),
        purchase.getAddress(),
        purchase.getPurchaseStatus()
    );
  }

}
