package com.nayoon.stock_service.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

  // ------ 4xx ------
  NOT_FOUND(HttpStatus.BAD_REQUEST, "요청사항을 찾지 못했습니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  STOCK_NOT_FOUND(HttpStatus.BAD_REQUEST, "재고 정보가 없습니다."),
  LIMIT_OF_STOCK(HttpStatus.BAD_REQUEST, "현재 재고가 초기 재고 한도를 초과할 수 없습니다."),
  OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "주문하려는 상품의 재고가 모두 소진되었습니다."),
  INVALID_NEW_STOCK(HttpStatus.BAD_REQUEST, "이미 소진된 재고보다 초기 재고를 작게 설정할 수 없습니다."),

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
