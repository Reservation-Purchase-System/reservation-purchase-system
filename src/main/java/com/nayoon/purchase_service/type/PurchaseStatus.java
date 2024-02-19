package com.nayoon.purchase_service.type;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PurchaseStatus {

  PENDING("pending"), // 결제 전
  PROCESSING("processing"), // 결제 중
  CONFIRMED("confirmed"); // 결제 완료

  private final String purchaseStatus;

  PurchaseStatus(String purchaseStatus) {
    this.purchaseStatus = purchaseStatus;
  }

  public static PurchaseStatus create(String purchaseStatus) {
    return Arrays.stream(values())
        .filter(value -> value.purchaseStatus.equalsIgnoreCase(purchaseStatus))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown purchaseStatus: " + purchaseStatus));
  }

}
