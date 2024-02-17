package com.nayoon.product_service.reservation_product.service.dto;

import com.nayoon.product_service.reservation_product.entity.ReservationProduct;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductDto(
    String name,
    String content,
    Long price,
    LocalDateTime reservedAt
) {

  public static ReservationProductDto toDto(ReservationProduct product) {
    return ReservationProductDto.builder()
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .reservedAt(product.getReservedAt())
        .build();
  }

}
