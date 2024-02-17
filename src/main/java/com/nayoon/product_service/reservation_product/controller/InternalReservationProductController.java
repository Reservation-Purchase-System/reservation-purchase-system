package com.nayoon.product_service.reservation_product.controller;

import com.nayoon.product_service.reservation_product.controller.dto.response.ReservationProductStockResponseDto;
import com.nayoon.product_service.reservation_product.service.ReservationProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/reservation-products")
public class InternalReservationProductController {

  private final ReservationProductService reservationProductService;

  /**
   * 예약상품 재고 조회
   */
  @GetMapping("/stock")
  public ResponseEntity<ReservationProductStockResponseDto> getProductStockById(
      @RequestParam(name = "id") Long productId
  ) {
    ReservationProductStockResponseDto response =
        ReservationProductStockResponseDto.dtoToResponseDto(reservationProductService.getProductStockById(productId));
    return ResponseEntity.ok().body(response);
  }

  /**
   * 예약 상품 재고 증가
   */
  @PostMapping("/stock/add")
  public ResponseEntity<Void> addReservationProductStock(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "quantity") Integer quantity
  ) {
    reservationProductService.addReservationProductStock(productId, quantity);
    return ResponseEntity.ok().build();
  }

  /**
   * 예약 상품 재고 감소
   */
  @PostMapping("/stock/subtract")
  public ResponseEntity<Void> subtractReservationProductStock(
      @RequestParam(name = "id") Long productId,
      @RequestParam(name = "quantity") Integer quantity
  ) {
    reservationProductService.subtractReservationProductStock(productId, quantity);
    return ResponseEntity.ok().build();
  }

}
