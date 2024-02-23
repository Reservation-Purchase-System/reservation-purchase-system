package com.nayoon.purchase_service.client;

import com.nayoon.purchase_service.client.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productClient", url = "${feign.productClient.url}")
public interface ProductClient {

  /**
   * product_service에 상품 정보 요청
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/products", consumes = "application/json")
  ProductResponseDto findProductInfo(@RequestParam(name = "id") Long productId);

}
