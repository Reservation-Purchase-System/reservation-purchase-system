package com.nayoon.payment_service.controller;

import com.nayoon.payment_service.controller.dto.request.PaymentCreateRequestDto;
import com.nayoon.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentService paymentService;

  /**
   * 결제 API 시작
   */
  @PostMapping
  public ResponseEntity<String> start(
      @RequestHeader("X-USER-ID") String userId,
      @Valid @RequestBody PaymentCreateRequestDto request
  ) {
    Long principalId = Long.valueOf(userId);
    double probability = Math.random();
    String result = paymentService.create(principalId, request.purchaseId(), probability);

    if ("fail".equals(result)) {
      return ResponseEntity.badRequest().body("Payment failed.");
    }

    return ResponseEntity.ok().body("Payment successful.");
  }

}
