package com.nayoon.payment_service.service;

import com.nayoon.payment_service.client.ProductClient;
import com.nayoon.payment_service.client.PurchaseClient;
import com.nayoon.payment_service.client.dto.PurchaseQuantityResponseDto;
import com.nayoon.payment_service.entity.Payment;
import com.nayoon.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final PurchaseClient purchaseClient;
  private final ProductClient productClient;

  /**
   * 결제 진행
   */
  public String create(Long userId, Long purchaseId) {
    Payment payment = Payment.builder()
        .purchaseId(purchaseId)
        .userId(userId)
        .build();

    Payment saved = paymentRepository.save(payment);

    // 80% 성공
    double probability = Math.random();
    if (0.2 < probability) {
      // purchase_service에 주문 상태 변경 요청
      purchaseClient.updatePurchaseStatus(purchaseId, "confirmed");
      return "success";
    }

    // 실패했을 경우 프로세스
    // 1. purchase_service에 주문 상태 변경 요청
    purchaseClient.updatePurchaseStatus(purchaseId, "cancelled");

    // 2. purchaseId로 상품 ID 및 주문 수량 반환
    PurchaseQuantityResponseDto quantityDto = purchaseClient.findProductIdByPurchaseId(purchaseId);

    // 2. product_service에 재고 변경 요청
    if ("product".equals(quantityDto.productType())) {
      productClient.addProductStock(quantityDto.productId(), quantityDto.quantity());
    } else {
      productClient.addReservationProductStock(quantityDto.productId(), quantityDto.quantity());
    }

    // deletedAt에 값 넣음으로써 삭제 처리
    saved.cancel();
    return "fail";
  }

}
