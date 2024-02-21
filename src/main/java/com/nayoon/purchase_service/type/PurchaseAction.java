package com.nayoon.purchase_service.type;

public enum PurchaseAction {

  CREATE("CREATE"),
  CANCEL("CANCEL");

  private final String value;

  PurchaseAction(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
