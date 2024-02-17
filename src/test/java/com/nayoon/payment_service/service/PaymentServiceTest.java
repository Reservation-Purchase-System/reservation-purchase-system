package com.nayoon.payment_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nayoon.payment_service.client.ProductClient;
import com.nayoon.payment_service.client.PurchaseClient;
import com.nayoon.payment_service.client.dto.PurchaseQuantityResponseDto;
import com.nayoon.payment_service.entity.Payment;
import com.nayoon.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  @InjectMocks
  private PaymentService paymentService;

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private PurchaseClient purchaseClient;

  @Mock
  private ProductClient productClient;

  @Nested
  @DisplayName("주문 API")
  class payment {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long userId = 1L;
      Long purchaseId = 1L;
      Payment payment = mockPayment(userId, purchaseId);
      double probability = 0.8;

      when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
        return payment;
      });

      //when
      String result = paymentService.create(userId, purchaseId, probability);

      //then
      assertEquals("success", result);
    }

    @Test
    @DisplayName("20% 확률로 결제 실패")
    void fail() {
      //given
      Long userId = 1L;
      Long purchaseId = 1L;
      Payment payment = mockPayment(userId, purchaseId);
      double probability = 0.2;

      when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
        return payment;
      });
      when(purchaseClient.findProductIdByPurchaseId(purchaseId)).thenAnswer(invocation -> {
        return mockPurchaseQuantityResponseDto(purchaseId);
      });

      //when
      String result = paymentService.create(userId, purchaseId, probability);

      //then
      assertEquals("fail", result);
    }

  }

  private PurchaseQuantityResponseDto mockPurchaseQuantityResponseDto(Long purchaseId) {
    return PurchaseQuantityResponseDto.builder()
        .productId(1L)
        .quantity(10)
        .productType("product")
        .build();
  }

  private Payment mockPayment(Long userId, Long purchaseId) {
    return Payment.builder()
        .purchaseId(purchaseId)
        .userId(userId)
        .build();
  }

}