package com.nayoon.product_service.reservation_product.service;

import com.nayoon.product_service.common.exception.CustomException;
import com.nayoon.product_service.common.exception.ErrorCode;
import com.nayoon.product_service.reservation_product.entity.ReservationProduct;
import com.nayoon.product_service.reservation_product.entity.ReservationProductStock;
import com.nayoon.product_service.reservation_product.repository.ReservationProductRepository;
import com.nayoon.product_service.reservation_product.repository.ReservationProductStockRepository;
import com.nayoon.product_service.reservation_product.service.dto.ReservationProductDto;
import com.nayoon.product_service.reservation_product.service.dto.ReservationProductStockDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationProductService {

  private final ReservationProductRepository reservationProductRepository;
  private final ReservationProductStockRepository reservationProductStockRepository;

  /**
   * 상품 등록
   */
  @Transactional
  public Long create(Long principalId, String name, String content, Long price, Integer stock,
      LocalDateTime reservedAt) {
    ReservationProduct reservationProduct = ReservationProduct.builder()
        .userId(principalId)
        .name(name)
        .content(content)
        .price(price)
        .reservedAt(reservedAt)
        .build();

    ReservationProduct saved = reservationProductRepository.save(reservationProduct);
    ReservationProductStock reservationProductStock = ReservationProductStock.builder()
        .productId(saved.getId())
        .stock(stock)
        .build();

    reservationProductStockRepository.save(reservationProductStock);

    return saved.getId();
  }

  /**
   * 상품 수정
   */
  @Transactional
  public void update(Long principalId, Long productId, String name, String content, Long price,
      Integer stock, LocalDateTime reservedAt) {
    ReservationProduct reservationProduct = reservationProductRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    checkProductOwner(principalId, reservationProduct);

    ReservationProductStock reservationProductStock = reservationProductStockRepository.findById(
            productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    reservationProduct.update(name, content, price, reservedAt);
    reservationProductStock.update(stock);
  }

  private void checkProductOwner(Long principalId, ReservationProduct reservationProduct) {
    if (!principalId.equals(reservationProduct.getUserId())) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }
  }

  /**
   * 상품 상세정보 조회
   */
  @Transactional(readOnly = true)
  public ReservationProductDto getProductInfoById(Long productId) {
    ReservationProduct product = reservationProductRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    return ReservationProductDto.toDto(product);
  }

  /**
   * 전체 상품 조회
   */
  @Transactional(readOnly = true)
  public Page<ReservationProduct> getAllProducts(Pageable pageable) {
    return reservationProductRepository.filterAllReservationProducts(pageable);
  }

  /**
   * 상품 재고 조회
   */
  // TODO: 결제 프로세스 진입한 수 제외하고 반환
  @Transactional(readOnly = true)
  public ReservationProductStockDto getProductStockById(Long productId) {
    ReservationProductStock productStock = reservationProductStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    ReservationProduct product = reservationProductRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    return ReservationProductStockDto.toDto(productStock, product.getReservedAt());
  }

  /**
   * 재고 증가
   */
  @Transactional
  public void addReservationProductStock(Long productId, Integer quantity) {
    ReservationProductStock productStock = reservationProductStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    productStock.addReservationProductStockByOrder(quantity);
  }

  /**
   * 재고 감소
   */
  @Transactional
  public void subtractReservationProductStock(Long productId, Integer quantity) {
    ReservationProductStock productStock = reservationProductStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    productStock.subtractReservationProductStockByOrder(quantity);
  }

}
