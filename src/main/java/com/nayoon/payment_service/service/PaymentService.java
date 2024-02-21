package com.nayoon.payment_service.service;

import com.nayoon.payment_service.client.PurchaseClient;
import com.nayoon.payment_service.client.StockClient;
import com.nayoon.payment_service.client.dto.PurchaseQuantityResponseDto;
import com.nayoon.payment_service.entity.Payment;
import com.nayoon.payment_service.entity.PaymentLogging;
import com.nayoon.payment_service.repository.PaymentLoggingRepository;
import com.nayoon.payment_service.repository.PaymentRepository;
import com.nayoon.payment_service.type.PaymentAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final PaymentLoggingRepository paymentLoggingRepository;
  private final PurchaseClient purchaseClient;
  private final StockClient stockClient;

  /**
   * 결제 시작
   */
  @Transactional
  public String create(Long userId, Long purchaseId, double probability) {
    Payment payment = Payment.builder()
        .purchaseId(purchaseId)
        .userId(userId)
        .build();
    paymentLoggingRepository.save(new PaymentLogging(payment, PaymentAction.START));
    Payment saved = paymentRepository.save(payment);

    // 80% 성공
    if (0.2 < probability) {
      return handleSuccess(saved);
    } else {
      return handleFailure(purchaseId, saved);
    }
  }

  private String handleSuccess(Payment payment) {
    paymentLoggingRepository.save(new PaymentLogging(payment, PaymentAction.COMPLETE));
    return "success";
  }

  private String handleFailure(Long purchaseId, Payment payment) {
    // 1. purchase_service에 주문 삭제 요청
    purchaseClient.delete(purchaseId);

    // 2. purchaseId로 상품 ID 및 주문 수량 반환
    PurchaseQuantityResponseDto quantityDto = purchaseClient.findProductIdByPurchaseId(purchaseId);

    // 3. product_service에 재고 변경 요청
    stockClient.increaseProductStock(quantityDto.productId(), quantityDto.quantity());

    // deletedAt에 값 넣어서 결제 취소 처리
    payment.cancel();
    paymentLoggingRepository.save(new PaymentLogging(payment, PaymentAction.CANCEL));
    return "fail";
  }

}
