package com.nayoon.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productClient", url = "${feign.productClient.url}")
public interface ProductClient {

  /**
   * 상품 재고 증가 요청
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/internal/products/stock/add", consumes = "application/json")
  void addProductStock(@RequestParam(name = "id") Long productId, @RequestParam(name = "quantity") Integer quantity);

  /**
   * 예약 상품 재고 증가 요청
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/internal/reservation-products/stock/add", consumes = "application/json")
  void addReservationProductStock(@RequestParam(name = "id") Long productId, @RequestParam(name = "quantity") Integer quantity);

}
