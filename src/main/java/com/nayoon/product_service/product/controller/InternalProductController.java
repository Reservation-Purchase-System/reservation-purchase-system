package com.nayoon.product_service.product.controller;

import com.nayoon.product_service.product.controller.dto.response.ProductResponseDto;
import com.nayoon.product_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/products")
public class InternalProductController {

  private final ProductService productService;

  /**
   * 상품 정보 조회
   */
  @GetMapping
  public ResponseEntity<ProductResponseDto> getProductInfoById(
      @RequestParam(name = "id") Long productId
  ) {
    ProductResponseDto response =
        ProductResponseDto.dtoToResponseDto(productService.getProductInfoById(productId));
    return ResponseEntity.ok().body(response);
  }

}
