package com.nayoon.product_service.product.service.dto;

import com.nayoon.product_service.product.entity.Product;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductDto(
    String name,
    String content,
    Long price,
    LocalDateTime openAt
) {

  public static ProductDto toDto(Product product) {
    return ProductDto.builder()
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .openAt(product.getOpenAt())
        .build();
  }

}
