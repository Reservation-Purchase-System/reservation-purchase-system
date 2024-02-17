package com.nayoon.product_service.reservation_product.controller.dto.response;

import com.nayoon.product_service.reservation_product.entity.ReservationProduct;
import com.nayoon.product_service.reservation_product.service.dto.ReservationProductDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReservationProductResponseDto(
    String name,
    String content,
    Long price,
    LocalDateTime reservedAt
) {

  public static ReservationProductResponseDto entityToResponseDto(ReservationProduct product) {
    return ReservationProductResponseDto.builder()
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .reservedAt(product.getReservedAt())
        .build();
  }

  public static ReservationProductResponseDto dtoToResponseDto(ReservationProductDto dto) {
    return ReservationProductResponseDto.builder()
        .name(dto.name())
        .content(dto.content())
        .price(dto.price())
        .reservedAt(dto.reservedAt())
        .build();
  }

}
