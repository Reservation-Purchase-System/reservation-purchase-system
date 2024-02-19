package com.nayoon.purchase_service.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nayoon.purchase_service.client.dto.ProductStockDto;
import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
public record ProductStockResponseDto(
    Integer stock,
    LocalDateTime openAt
) {

  public static ProductStockResponseDto toResponseDto(ProductStockDto dto) {
    return new ProductStockResponseDto(
        dto.stock(),
        dto.openAt()
    );
  }

}
