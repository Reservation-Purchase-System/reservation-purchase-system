package com.nayoon.product_service.reservation_product.controller;

import com.nayoon.product_service.reservation_product.controller.dto.request.ReservationProductCreateRequestDto;
import com.nayoon.product_service.reservation_product.controller.dto.request.ReservationProductUpdateRequestDto;
import com.nayoon.product_service.reservation_product.controller.dto.response.ReservationProductResponseDto;
import com.nayoon.product_service.reservation_product.controller.dto.response.ReservationProductStockResponseDto;
import com.nayoon.product_service.reservation_product.service.ReservationProductService;
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
@RequestMapping("/api/v1/reservation-products")
public class ReservationProductController {

  private final ReservationProductService reservationProductService;

  /**
   * 예약 상품 등록
   */
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestHeader("X-USER-ID") String userId,
      @Valid @RequestBody ReservationProductCreateRequestDto request
  ) {
    Long principalId = Long.valueOf(userId);
    Long productId = reservationProductService.create(principalId, request.name(), request.content(),
        request.price(), request.stock(), request.reservedAt());
    return ResponseEntity.created(URI.create("api/v1/products/" + productId)).build();
  }

  /**
   * 예약 상품 수정
   */
  @PatchMapping("/{id}")
  public ResponseEntity<Void> update(
      @RequestHeader("X-USER-ID") String userId,
      @PathVariable(name = "id") Long productId,
      @Valid @RequestBody ReservationProductUpdateRequestDto request
  ) {
    Long principalId = Long.valueOf(userId);
    reservationProductService.update(principalId, productId, request.name(), request.content(),
        request.price(), request.stock(), request.reservedAt());
    return ResponseEntity.ok().build();
  }

  /**
   * 예약 상품 상세정보 조회
   */
  @GetMapping("/{id}")
  public ResponseEntity<ReservationProductResponseDto> getProductInfoById(
      @PathVariable(name = "id") Long productId
  ) {
    ReservationProductResponseDto response =
        ReservationProductResponseDto.dtoToResponseDto(reservationProductService.getProductInfoById(productId));
    return ResponseEntity.ok().body(response);
  }

  /**
   * 전체 예약상품 조회
   */
  @GetMapping
  public ResponseEntity<Page<ReservationProductResponseDto>> getAllProducts(
      @RequestParam(value = "orderBy", defaultValue = "reservedAt") String orderBy,
      @RequestParam(value = "sortBy", defaultValue = "desc") String sortBy,
      @RequestParam(value = "pageCount", defaultValue = "20") int size,
      @RequestParam(value = "page", defaultValue = "0") int page
  ) {
    Sort.Direction direction = sortBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, orderBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<ReservationProductResponseDto> products = reservationProductService.getAllProducts(pageable)
        .map(ReservationProductResponseDto::entityToResponseDto);
    return ResponseEntity.ok().body(products);
  }

  /**
   * 예약상품 재고 조회
   */
  @GetMapping("/{id}/stock")
  public ResponseEntity<ReservationProductStockResponseDto> getProductStockById(
      @PathVariable(name = "id") Long productId
  ) {
    ReservationProductStockResponseDto response =
        ReservationProductStockResponseDto.dtoToResponseDto(reservationProductService.getProductStockById(productId));
    return ResponseEntity.ok().body(response);
  }

}
