package com.nayoon.product_service.product.controller;

import com.nayoon.product_service.product.controller.dto.request.ProductCreateRequestDto;
import com.nayoon.product_service.product.controller.dto.request.ProductUpdateRequestDto;
import com.nayoon.product_service.product.controller.dto.response.ProductResponseDto;
import com.nayoon.product_service.product.controller.dto.response.ProductStockResponseDto;
import com.nayoon.product_service.product.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  /**
   * 상품 등록
   */
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestHeader("X-USER-ID") String userId,
      @Valid @RequestBody ProductCreateRequestDto request
  ) {
    Long principalId = Long.valueOf(userId);

    Long productId =
        productService.create(principalId, request.name(), request.content(), request.price(),
            request.stock());

    return ResponseEntity.created(URI.create("api/v1/products/" + productId)).build();
  }

  /**
   * 상품 수정
   */
  @PatchMapping("/{id}")
  public ResponseEntity<Void> update(
      @RequestHeader("X-USER-ID") String userId,
      @PathVariable(name = "id") Long productId,
      @Valid @RequestBody ProductUpdateRequestDto request
  ) {
    Long principalId = Long.valueOf(userId);
    productService.update(principalId, productId, request.name(), request.content(),
        request.price(), request.stock());
    return ResponseEntity.ok().build();
  }

  /**
   * 상품 상세정보 조회
   */
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getProductInfoById(
      @PathVariable(name = "id") Long productId
  ) {
    ProductResponseDto response =
        ProductResponseDto.dtoToResponseDto(productService.getProductInfoById(productId));
    return ResponseEntity.ok().body(response);
  }

  /**
   * 전체 상품 조회
   */
  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
      @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
      @RequestParam(value = "sortBy", defaultValue = "desc") String sortBy,
      @RequestParam(value = "pageCount", defaultValue = "20") int size,
      @RequestParam(value = "page", defaultValue = "0") int page
  ) {
    Sort.Direction direction =
        sortBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, orderBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<ProductResponseDto> products = productService.getAllProducts(pageable)
        .map(ProductResponseDto::entityToResponseDto);
    return ResponseEntity.ok().body(products);
  }

  /**
   * 상품 재고 조회
   */
  @GetMapping("/{id}/stock")
  public ResponseEntity<ProductStockResponseDto> getProductStockById(
      @PathVariable(name = "id") Long productId
  ) {
    ProductStockResponseDto response =
        ProductStockResponseDto.dtoToResponseDto(productService.getProductStockById(productId));
    return ResponseEntity.ok().body(response);
  }

}
