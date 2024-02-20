package com.nayoon.stock_service.controller.dto;

public record StockResponseDto(
    Integer stock
) {

  public static StockResponseDto valueToDto(Integer stock) {
    return new StockResponseDto(stock);
  }

}
