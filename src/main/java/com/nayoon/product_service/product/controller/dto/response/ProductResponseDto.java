package com.nayoon.product_service.product.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nayoon.product_service.product.entity.Product;
import com.nayoon.product_service.product.service.dto.ProductDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDto(
    String name,
    String content,
    Long price,
    Boolean isReserved,
    LocalDateTime openAt
) {

  public static ProductResponseDto entityToResponseDto(Product product) {
    return ProductResponseDto.builder()
        .name(product.getName())
        .content(product.getContent())
        .price(product.getPrice())
        .isReserved(product.getIsReserved())
        .openAt(product.getOpenAt())
        .build();
  }

  public static ProductResponseDto dtoToResponseDto(ProductDto productDto) {
    return ProductResponseDto.builder()
        .name(productDto.name())
        .content(productDto.content())
        .price(productDto.price())
        .openAt(productDto.openAt())
        .build();
  }

}
