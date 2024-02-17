package com.nayoon.purchase_service.client;

import com.nayoon.purchase_service.client.dto.ReservationProductStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productClient", url = "${feign.productClient.url}")
public interface ProductClient {

  /**
   * product_service에 일반 상품 재고 정보 요청
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/products/stock", consumes = "application/json")
  Integer findProductStock(@RequestParam(name = "id") Long productId);

  /**
   * product_service에 예약 상품 재고 정보 요청
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/reservation-products/stock", consumes = "application/json")
  ReservationProductStockDto findReservationProductStock(@RequestParam(name = "id") Long productId);

  /**
   * product_service에 상품 재고 감소 요청
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/internal/products/stock/subtract", consumes = "application/json")
  void subtractProductStock(@RequestParam(name = "id") Long productId, @RequestParam(name = "quantity") Integer quantity);

  /**
   * product_service에 상품 재고 감소 요청
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/internal/reservation-products/stock/subtract", consumes = "application/json")
  void subtractReservationProductStock(@RequestParam(name = "id") Long productId, @RequestParam(name = "quantity") Integer quantity);

}
