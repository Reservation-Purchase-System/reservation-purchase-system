package com.nayoon.purchase_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nayoon.purchase_service.client.ProductClient;
import com.nayoon.purchase_service.client.StockClient;
import com.nayoon.purchase_service.client.dto.ProductResponseDto;
import com.nayoon.purchase_service.common.exception.CustomException;
import com.nayoon.purchase_service.common.exception.ErrorCode;
import com.nayoon.purchase_service.controller.dto.request.PurchaseCreateRequestDto;
import com.nayoon.purchase_service.entity.Purchase;
import com.nayoon.purchase_service.entity.PurchaseLogging;
import com.nayoon.purchase_service.repository.PurchaseLoggingRepository;
import com.nayoon.purchase_service.repository.PurchaseRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

  @InjectMocks
  private PurchaseService purchaseService;

  @Mock
  private PurchaseRepository purchaseRepository;

  @Mock
  private PurchaseLoggingRepository purchaseLoggingRepository;

  @Mock
  private ProductClient productClient;

  @Mock
  private StockClient stockClient;

  @Nested
  @DisplayName("일반 상품 주문 생성")
  class createProduct {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long userId = 1L;
      PurchaseCreateRequestDto request = mockPurchaseCreateRequest();
      Purchase purchase = mockPurchase(userId);

      when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
        return purchase;
      });
      when(productClient.findProductInfo(anyLong())).thenReturn(mock(ProductResponseDto.class));
      when(stockClient.getRemainingProductStock(anyLong())).thenReturn(100);

      //when
      Long purchaseId = purchaseService.create(userId, request.productId(), request.quantity(),
          request.address());

      //then
      assertEquals(purchaseId, purchase.getId());
      verify(purchaseRepository, times(1)).save(any(Purchase.class));
      verify(purchaseLoggingRepository, times(1)).save(any(PurchaseLogging.class));
    }

    @Test
    @DisplayName("실패: 재고 없음")
    void insufficientStock() {
      //given
      Long userId = 1L;
      PurchaseCreateRequestDto request = mockPurchaseCreateRequest();

      when(productClient.findProductInfo(anyLong())).thenReturn(mock(ProductResponseDto.class));
      when(stockClient.getRemainingProductStock(anyLong())).thenReturn(0);

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> purchaseService.create(userId, request.productId(), request.quantity(),
          request.address()));

      //then
      assertEquals(ErrorCode.INSUFFICIENT_STOCK, exception.getErrorCode());
    }

  }

  @Nested
  @DisplayName("예약 상품 주문 생성")
  class createReservationProduct {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long userId = 1L;
      PurchaseCreateRequestDto request = mockReservationPurchaseCreateRequest();
      Purchase purchase = mockReservationPurchase(userId);
      // 예약 상품의 예약 시간을 현재 시간으로부터 한 시간 전으로 설정
      LocalDateTime openAt = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
      ProductResponseDto responseDto = mockProductResponseDto(openAt);

      when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
        return purchase;
      });
      when(productClient.findProductInfo(anyLong())).thenReturn(responseDto);
      when(stockClient.getRemainingProductStock(anyLong())).thenReturn(100);

      //when
      Long purchaseId = purchaseService.create(userId, request.productId(), request.quantity(),
          request.address());

      //then
      assertEquals(purchaseId, purchase.getId());
      verify(purchaseRepository, times(1)).save(any(Purchase.class));
      verify(purchaseLoggingRepository, times(1)).save(any(PurchaseLogging.class));
    }

    @Test
    @DisplayName("실패: 재고 없음")
    void insufficientStock() {
      //given
      Long userId = 1L;
      PurchaseCreateRequestDto request = mockReservationPurchaseCreateRequest();
      LocalDateTime openAt = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
      ProductResponseDto responseDto = mockProductResponseDto(openAt);

      when(productClient.findProductInfo(anyLong())).thenReturn(responseDto);
      when(stockClient.getRemainingProductStock(anyLong())).thenReturn(0);

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> purchaseService.create(userId, request.productId(), request.quantity(),
          request.address()));

      //then
      assertEquals(ErrorCode.INSUFFICIENT_STOCK, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패: 판매 예약 시간이 아님")
    void purchaseNotAvailable() {
      //given
      Long userId = 1L;
      PurchaseCreateRequestDto request = mockReservationPurchaseCreateRequest();
      LocalDateTime openAt = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
      ProductResponseDto responseDto = mockProductResponseDto(openAt);

      when(productClient.findProductInfo(anyLong())).thenReturn(responseDto);

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> purchaseService.create(userId, request.productId(), request.quantity(),
          request.address()));

      //then
      assertEquals(ErrorCode.PURCHASE_NOT_AVAILABLE, exception.getErrorCode());
    }

  }

  @Nested
  @DisplayName("주문 조회")
  class getPurchasesByUserId {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long userId = 1L;
      Pageable pageable = Pageable.ofSize(10).withPage(0);
      Purchase deletedPurchase = mockPurchase(userId);

      deletedPurchase.cancel();

      List<Purchase> purchases = Arrays.asList(
          deletedPurchase,
          mockPurchase2(userId)
      );

      when(purchaseRepository.getPurchasesByUserId(userId, pageable)).thenAnswer(invocation -> {
        List<Purchase> confirmedPurchases = purchases.stream()
            .filter(purchase -> purchase.getDeletedAt() == null)
            .collect(Collectors.toList());
        return new PageImpl<>(confirmedPurchases);
      });

      //when
      Page<Purchase> result = purchaseService.getPurchasesByUserId(userId, pageable);

      //then
      assertEquals(1, result.getContent().size());
      assertEquals(purchases.get(1), result.getContent().get(0));
    }

  }

  private ProductResponseDto mockProductResponseDto(LocalDateTime openAt) {
    return ProductResponseDto.builder()
        .name("상품1")
        .content("이것은 상품1입니다.")
        .isReserved(true)
        .price(1000L)
        .openAt(openAt)
        .build();
  }

  private Purchase mockPurchase(Long userId) {
    return Purchase.builder()
        .userId(userId)
        .productId(1L)
        .quantity(5)
        .address("address1")
        .build();
  }

  private Purchase mockPurchase2(Long userId) {
    return Purchase.builder()
        .userId(userId)
        .productId(2L)
        .quantity(5)
        .address("address1")
        .build();
  }

  private Purchase mockReservationPurchase(Long userId) {
    return Purchase.builder()
        .userId(userId)
        .productId(1L)
        .quantity(5)
        .address("address1")
        .build();
  }

  private PurchaseCreateRequestDto mockPurchaseCreateRequest() {
    return PurchaseCreateRequestDto.builder()
        .productId(1L)
        .quantity(5)
        .address("address1")
        .build();
  }

  private PurchaseCreateRequestDto mockReservationPurchaseCreateRequest() {
    return PurchaseCreateRequestDto.builder()
        .productId(1L)
        .quantity(5)
        .address("address1")
        .build();
  }

}