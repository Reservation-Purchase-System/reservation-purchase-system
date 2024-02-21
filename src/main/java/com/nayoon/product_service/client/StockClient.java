package com.nayoon.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stockClient", url = "${feign.stockClient.url}")
public interface StockClient {

  /**
   * 재고 생성 or 업데이트 요청
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/internal/stocks", consumes = "application/json")
  void createOrUpdate(@RequestParam("id") Long productId, @RequestParam("stock") Integer stock);

}
