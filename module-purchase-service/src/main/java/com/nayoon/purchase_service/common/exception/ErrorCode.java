package com.nayoon.purchase_service.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

  // ------ 4xx ------
  NOT_FOUND(HttpStatus.BAD_REQUEST, "요청사항을 찾지 못했습니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),
  PURCHASE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "구매 가능한 시간이 아닙니다."),
  PURCHASE_NOT_FOUND(HttpStatus.BAD_REQUEST, "구매 내역을 찾을 수 없습니다."),

  // ------ 5xx ------
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
  ;

  private final HttpStatus status;
  private final String message;

  ErrorCode(final HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

}
