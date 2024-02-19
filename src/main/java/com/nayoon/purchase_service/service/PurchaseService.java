package com.nayoon.purchase_service.service;

import com.nayoon.purchase_service.client.ProductClient;
import com.nayoon.purchase_service.common.exception.CustomException;
import com.nayoon.purchase_service.common.exception.ErrorCode;
import com.nayoon.purchase_service.entity.Purchase;
import com.nayoon.purchase_service.repository.PurchaseRepository;
import com.nayoon.purchase_service.service.dto.ProductStockResponseDto;
import com.nayoon.purchase_service.service.dto.PurchaseQuantityDto;
import com.nayoon.purchase_service.type.PurchaseStatus;
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
  private final ProductClient productClient;

  /**
   * 주문 생성
   * - 결제하기 버튼 클릭 시 요청
   */
  @Transactional
  public Long create(Long principalId, Long productId, Integer quantity, String address, String purchaseStatus) {
    // 주문 시 재고 확인
    checkAvailableStock(quantity, productId);

    Purchase purchase = Purchase.builder()
        .userId(principalId)
        .productId(productId)
        .quantity(quantity)
        .address(address)
        .purchaseStatus(PurchaseStatus.create(purchaseStatus))
        .build();

    Purchase savedPurchaseEntity = purchaseRepository.save(purchase);

    // 주문 생성이 성공한 경우에만 재고 감소
    productClient.decreaseProductStock(productId, quantity);

    return savedPurchaseEntity.getId();
  }

  // 남은 재고를 파악하여 주문 가능한 수량인지 확인
  private void checkAvailableStock(Integer orderQuantity, Long productId) {
    // 재고 수량 및 오픈 시간
    ProductStockResponseDto dto = ProductStockResponseDto.toResponseDto(productClient.findProductStock(productId));
    Integer stock = dto.stock();

    if (dto.openAt() != null && dto.openAt().isAfter(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.PURCHASE_NOT_AVAILABLE);
    }

    // 재고가 주문 quantity보다 적다면 예외 발생
    if (stock < orderQuantity) {
      throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
    }
  }

  /**
   * 주문 조회
   * - 결제까지 완료한 주문만 전달
   */
  @Transactional(readOnly = true)
  public Page<Purchase> getPurchasesByUserId(Long principalId, Pageable pageable) {
    return purchaseRepository.getOrdersByUserId(principalId, pageable);
  }

  /**
   * 주문 상태 변경
   */
  @Transactional
  public void updateStatus(Long purchaseId, String purchaseStatus) {
    Purchase purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_NOT_FOUND));

    purchase.updateStatus(PurchaseStatus.create(purchaseStatus));
  }

  @Transactional
  public PurchaseQuantityDto findProductIdByPurchaseId(Long purchaseId) {
    Purchase purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_NOT_FOUND));

    return PurchaseQuantityDto.entityToDto(purchase);
  }

  /**
   * 주문 취소
   */
  @Transactional
  public void cancel(Long purchaseId) {
    Purchase purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_NOT_FOUND));

    purchase.cancel();
  }

}
