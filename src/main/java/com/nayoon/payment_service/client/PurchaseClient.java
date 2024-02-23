package com.nayoon.payment_service.client;

import com.nayoon.payment_service.client.dto.PurchaseQuantityResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "purchaseClient", url = "${feign.purchaseClient.url}")
public interface PurchaseClient {

  /**
   * 주문 상태 변경 요청
   */
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/internal/purchases", consumes = "application/json")
  void updatePurchaseStatus(@RequestParam(name = "id") Long purchaseId, @RequestParam(name = "status") String purchaseStatus);

  /**
   * 주문 ID로 상품 ID 검색
   */
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/purchases", consumes = "application/json")
  PurchaseQuantityResponseDto findProductIdByPurchaseId(@RequestParam(name = "id") Long purchaseId);

  /**
   * 주문 삭제 요청
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/internal/purchases", consumes = "application/json")
  void delete(@RequestParam(name = "id") Long purchaseId);

}
