package com.nayoon.product_service.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

  // ------ 4xx ------
  NOT_FOUND(HttpStatus.BAD_REQUEST, "요청사항을 찾지 못했습니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
  PRODUCT_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 재고를 찾을 수 없습니다."),
  RESERVED_PRODUCT_MUST_HAVE_OPEN_AT_FIELD(HttpStatus.BAD_REQUEST, "예약 상품은 상품 판매 시작 시간을 입력해야 합니다."),
  CANNOT_HANDLE_PRODUCT_STOCK_NOW(HttpStatus.BAD_REQUEST, "아직 주문 가능한 시간이 아닙니다."),

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
