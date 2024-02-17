package com.nayoon.product_service.product.service.dto;

import com.nayoon.product_service.product.entity.Product;
import lombok.Builder;

@Builder
public record ProductDto(
    String name,
    String content,
    Long price
) {

  public static ProductDto toDto(Product product) {
    return ProductDto.builder()
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .build();
  }

}
