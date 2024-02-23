package com.nayoon.payment_service.client;

import com.nayoon.payment_service.client.dto.PurchaseQuantityResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "purchaseClient", url = "${feign.purchaseClient.url}")
public interface PurchaseClient {

  /**
   * 주문 ID로 상품 ID 검색
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/purchases/{purchaseId}", consumes = "application/json")
  PurchaseQuantityResponseDto findProductIdByPurchaseId(@PathVariable(name = "purchaseId") Long purchaseId);

  /**
   * 주문 삭제 요청
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/internal/purchases", consumes = "application/json")
  void delete(@RequestParam(name = "id") Long purchaseId);

}
