package com.nayoon.purchase_service.type;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ProductType {

  PRODUCT("product"),
  RESERVATION_PRODUCT("reservationProduct");

  private final String productType;

  ProductType(String productType) {
    this.productType = productType;
  }

  public static ProductType create(String productType) {
    return Arrays.stream(values())
        .filter(value -> value.productType.equalsIgnoreCase(productType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown ProductType: " + productType));
  }

}