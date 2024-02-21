package com.nayoon.payment_service.type;

public enum PaymentAction {

  START("START"),
  CANCEL("CANCEL"),
  COMPLETE("COMPLETE");

  private final String value;

  PaymentAction(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
