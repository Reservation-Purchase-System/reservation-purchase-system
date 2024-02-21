package com.nayoon.purchase_service.service.dto;

import com.nayoon.purchase_service.client.dto.ProductResponseDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductDto(
    String name,
    String content,
    Long price,
    Boolean isReserved,
    LocalDateTime openAt
) {

  public static ProductDto responseToDto(ProductResponseDto dto) {
    return ProductDto.builder()
        .name(dto.name())
        .content(dto.content())
        .price(dto.price())
        .isReserved(dto.isReserved())
        .openAt(dto.openAt())
        .build();
  }

}
