package com.nayoon.product_service.product.controller.dto.response;

import com.nayoon.product_service.product.entity.Product;
import com.nayoon.product_service.product.service.dto.ProductDto;
import lombok.Builder;

@Builder
public record ProductResponseDto(
    String name,
    String content,
    Long price
) {

  public static ProductResponseDto entityToResponseDto(Product product) {
    return ProductResponseDto.builder()
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .build();
  }

  public static ProductResponseDto dtoToResponseDto(ProductDto productDto) {
    return ProductResponseDto.builder()
        .name(productDto.name())
        .content(productDto.content())
        .price(productDto.price())
        .build();
  }

}
