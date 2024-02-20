package com.nayoon.stock_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nayoon.stock_service.client.PurchaseClient;
import com.nayoon.stock_service.common.exception.CustomException;
import com.nayoon.stock_service.common.exception.ErrorCode;
import com.nayoon.stock_service.entity.Stock;
import com.nayoon.stock_service.repository.StockRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

  @InjectMocks
  private StockService stockService;

  @Mock
  private StockRepository stockRepository;

  @Mock
  private PurchaseClient purchaseClient;

  @Nested
  @DisplayName("재고 생성")
  class create {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long productId = 1L;
      Integer initialStock = 100;
      Integer remainingStock = 0;

      Stock stock = mockStock(productId, remainingStock, initialStock);

      when(stockRepository.existsByProductId(productId)).thenReturn(false);
      when(stockRepository.save(any())).thenReturn(stock);

      //when
      Integer result = stockService.createOrUpdate(productId, initialStock);

      //then
      verify(stockRepository, times(1)).existsByProductId(productId);
      verify(stockRepository, times(1)).save(any());
      assertEquals(initialStock, result);
    }

  }

  @Nested
  @DisplayName("재고 업데이트")
  class update {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long productId = 1L;
      Integer oldStock = 300;
      Integer newStock = 100;
      Integer remainingStock = 0;

      Stock existingStock = mockStock(productId, remainingStock, oldStock);
      Stock stock = mockStock(productId, remainingStock, newStock);

      when(stockRepository.existsByProductId(productId)).thenReturn(true);
      when(stockRepository.findByProductId(productId)).thenReturn(
          Optional.ofNullable(existingStock));
      when(stockRepository.save(any())).thenReturn(stock);
      when(purchaseClient.getQuantitySumByProductId(productId)).thenReturn(10);

      //when
      Integer result = stockService.createOrUpdate(productId, newStock);

      //then
      verify(stockRepository, times(1)).findByProductId(productId);
      verify(stockRepository, times(1)).existsByProductId(productId);
      verify(stockRepository, times(1)).save(any());
      assertEquals(newStock - 10, result); // 결제 프로세스 진입한 quantity 합 빼기
    }

    @Test
    @DisplayName("실패: 재고 정보 찾을 수 없음")
    void stockNotFound() {
      //given
      Long productId = 1L;
      Integer newStock = 100;

      when(stockRepository.existsByProductId(productId)).thenReturn(true);
      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> stockService.createOrUpdate(productId, newStock));

      //then
      assertEquals(ErrorCode.STOCK_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패: 결제 프로세스의 합보다 재고가 작을 수 없음")
    void invalidNewStock() {
      //given
      Long productId = 1L;
      Integer oldStock = 300;
      Integer newStock = 5;
      Integer remainingStock = 0;
      Stock existingStock = mockStock(productId, remainingStock, oldStock);

      when(stockRepository.existsByProductId(productId)).thenReturn(true);
      when(stockRepository.findByProductId(productId)).thenReturn(
          Optional.ofNullable(existingStock));
      when(purchaseClient.getQuantitySumByProductId(productId)).thenReturn(10);

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> stockService.createOrUpdate(productId, newStock));

      //then
      assertEquals(ErrorCode.INVALID_NEW_STOCK, exception.getErrorCode());
    }

  }

  @Nested
  @DisplayName("남은 재고 증가")
  class increaseStock {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long productId = 1L;
      Integer quantity = 5;
      Integer remainingStock = 0;
      Integer initialStock = 10;
      Stock stock = mockStock(productId, remainingStock, initialStock);

      when(stockRepository.findByProductId(productId)).thenReturn(
          Optional.ofNullable(stock));

      //when
      Integer result = stockService.increaseStock(productId, quantity);

      //then
      verify(stockRepository, times(1)).findByProductId(productId);
      assertEquals(quantity, result);
    }

    @Test
    @DisplayName("실패: 남은 재고가 초기 재고보다 클 수 없음")
    void limitOfStock() {
      //given
      Long productId = 1L;
      Integer quantity = 5;
      Integer remainingStock = 0;
      Integer initialStock = 1;
      Stock stock = mockStock(productId, remainingStock, initialStock);

      when(stockRepository.findByProductId(productId)).thenReturn(
          Optional.ofNullable(stock));

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> stockService.increaseStock(productId, quantity));

      //then
      assertEquals(ErrorCode.LIMIT_OF_STOCK, exception.getErrorCode());
    }

  }

  @Nested
  @DisplayName("남은 재고 감소")
  class decreaseStock {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long productId = 1L;
      Integer quantity = 5;
      Integer remainingStock = 5;
      Integer initialStock = 10;
      Stock stock = mockStock(productId, remainingStock, initialStock);

      when(stockRepository.findByProductId(productId)).thenReturn(
          Optional.ofNullable(stock));

      //when
      Integer result = stockService.decreaseStock(productId, quantity);

      //then
      verify(stockRepository, times(1)).findByProductId(productId);
      assertEquals(remainingStock - quantity, result);
    }

    @Test
    @DisplayName("실패: 주문할 수 있는 재고가 없음")
    void outOfStock() {
      //given
      Long productId = 1L;
      Integer quantity = 5;
      Integer remainingStock = 0;
      Integer initialStock = 10;
      Stock stock = mockStock(productId, remainingStock, initialStock);

      when(stockRepository.findByProductId(productId)).thenReturn(
          Optional.ofNullable(stock));

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> stockService.decreaseStock(productId, quantity));

      //then
      assertEquals(ErrorCode.OUT_OF_STOCK, exception.getErrorCode());
    }

  }

  private Stock mockStock(Long productId, Integer stock, Integer initialStock) {
    return Stock.builder()
        .productId(productId)
        .stock(stock)
        .initialStock(initialStock)
        .build();
  }

}