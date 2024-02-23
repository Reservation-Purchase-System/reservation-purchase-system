package com.nayoon.purchase_service.service;

import com.nayoon.purchase_service.client.ProductClient;
import com.nayoon.purchase_service.client.StockClient;
import com.nayoon.purchase_service.common.exception.CustomException;
import com.nayoon.purchase_service.common.exception.ErrorCode;
import com.nayoon.purchase_service.entity.Purchase;
import com.nayoon.purchase_service.entity.PurchaseLogging;
import com.nayoon.purchase_service.repository.PurchaseLoggingRepository;
import com.nayoon.purchase_service.repository.PurchaseRepository;
import com.nayoon.purchase_service.service.dto.ProductDto;
import com.nayoon.purchase_service.service.dto.PurchaseQuantityDto;
import com.nayoon.purchase_service.type.PurchaseAction;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final PurchaseRepository purchaseRepository;
  private final PurchaseLoggingRepository purchaseLoggingRepository;
  private final ProductClient productClient;
  private final StockClient stockClient;

  /**
   * 주문 생성 - 결제 프로세스 진입
   */
  @Transactional
  public Long create(Long principalId, Long productId, Integer quantity, String address) {
    // 주문 시 재고 및 주문 가능한 시간인지 확인
    checkPurchaseAvailable(quantity, productId);

    // 상품 정보 요청
    ProductDto productDto = ProductDto.responseToDto(productClient.findProductInfo(productId));

    Purchase purchase = Purchase.builder()
        .userId(principalId)
        .productId(productId)
        .quantity(quantity)
        .price(productDto.price() * quantity)
        .address(address)
        .build();

    Purchase savedPurchase = purchaseRepository.save(purchase);

    // 주문 생성이 성공한 경우에만 재고 감소
    stockClient.decreaseProductStock(productId, quantity);

    // 로깅 테이블에 저장
    purchaseLoggingRepository.save(new PurchaseLogging(purchase, PurchaseAction.CREATE));

    return savedPurchase.getId();
  }

  // 남은 재고를 파악하여 주문 가능한 수량인지 확인
  private void checkPurchaseAvailable(Integer orderQuantity, Long productId) {
    // 오픈 시간
    ProductDto productDto = ProductDto.responseToDto(productClient.findProductInfo(productId));
    LocalDateTime openAt = productDto.openAt();

    if (openAt != null && openAt.isAfter(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.PURCHASE_NOT_AVAILABLE);
    }

    // 재고 수량
    Integer stock = stockClient.getRemainingProductStock(productId);

    // 재고가 주문 quantity보다 적다면 예외 발생
    if (stock < orderQuantity) {
      throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
    }
  }

  /**
   * 주문 취소 - 결제 프로세스 이탈
   */
  @Transactional
  public void cancel(Long purchaseId) {
    Purchase purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_NOT_FOUND));

    purchase.cancel();

    // 결제 이탈 시 감소시켰던 재고 증가
    stockClient.increaseProductStock(purchase.getProductId(), purchase.getQuantity());

    // 로깅 테이블에 저장
    purchaseLoggingRepository.save(new PurchaseLogging(purchase, PurchaseAction.CANCEL));
  }

  /**
   * 주문 조회
   */
  @Transactional(readOnly = true)
  public Page<Purchase> getPurchasesByUserId(Long principalId, Pageable pageable) {
    return purchaseRepository.getPurchasesByUserId(principalId, pageable);
  }

  /**
   * 주문 ID로 상품 ID 조회
   */
  @Transactional(readOnly = true)
  public PurchaseQuantityDto findProductIdByPurchaseId(Long purchaseId) {
    Purchase purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_NOT_FOUND));

    return PurchaseQuantityDto.entityToDto(purchase);
  }

  /**
   * 상품ID로 모든 주문량 조회
   */
  @Transactional(readOnly = true)
  public Integer getQuantitySumByProductId(Long productId) {
    Integer quantitySum = purchaseRepository.getQuantitySumByProductId(productId);

    // quantitySum이 null인 경우 0으로 처리
    quantitySum = quantitySum != null ? quantitySum : 0;

    return quantitySum;
  }

}
