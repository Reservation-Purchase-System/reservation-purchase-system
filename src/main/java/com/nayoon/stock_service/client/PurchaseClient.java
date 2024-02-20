package com.nayoon.stock_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "purchaseClient", url = "${feign.purchaseClient.url}")
public interface PurchaseClient {

  /**
   * 결제 프로세스 진입한 주문 조회
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/purchases/{productId}/quantity", consumes = "application/json")
  Integer getQuantitySumByProductId(@PathVariable("productId") Long productId);

}
