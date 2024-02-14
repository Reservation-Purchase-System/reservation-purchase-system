package com.nayoon.product_service.product.service;

import com.nayoon.product_service.common.exception.CustomException;
import com.nayoon.product_service.common.exception.ErrorCode;
import com.nayoon.product_service.product.entity.Product;
import com.nayoon.product_service.product.entity.ProductStock;
import com.nayoon.product_service.product.repository.ProductRepository;
import com.nayoon.product_service.product.repository.ProductStockRepository;
import com.nayoon.product_service.product.service.dto.ProductDto;
import com.nayoon.product_service.product.service.dto.ProductStockDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductStockRepository productStockRepository;

  /**
   * 상품 등록
   */
  @Transactional
  public Long create(Long principalId, String name, String content, Long price, Integer stock) {
    Product product = Product.builder()
        .userId(principalId)
        .name(name)
        .content(content)
        .price(price)
        .build();

    Product saved = productRepository.save(product);
    ProductStock productStock = ProductStock.builder()
        .productId(saved.getId())
        .stock(stock)
        .build();

    productStockRepository.save(productStock);

    return saved.getId();
  }

  /**
   * 상품 수정
   */
  @Transactional
  public void update(Long principalId, Long productId, String name, String content, Long price, Integer stock) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    ProductStock productStock = productStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    checkProductOwner(principalId, product);

    product.update(name, content, price);
    productStock.update(stock);
  }

  private void checkProductOwner(Long principalId, Product product) {
    if (!principalId.equals(product.getUserId())) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }
  }

  /**
   * 상품 상세정보 조회
   */
  @Transactional
  public ProductDto getProductInfoById(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    return ProductDto.toDto(product);
  }

  /**
   * 전체 상품 조회
   */
  public Page<Product> getAllProducts(Pageable pageable) {
    return productRepository.filterAllProducts(pageable);
  }

  /**
   * 상품 재고 조회
   */
  // TODO: 결제 프로세스 진입한 수 제외하고 반환
  public ProductStockDto getProductStockById(Long productId) {
    ProductStock productStock = productStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    return ProductStockDto.toDto(productStock);
  }

  /**
   * 재고 증가
   */
  @Transactional
  public void addProductStock(Long productId, Integer quantity) {
    ProductStock productStock = productStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    productStock.addProductStockByOrder(quantity);
  }

  /**
   * 재고 감소
   */
  @Transactional
  public void subtractProductStock(Long productId, Integer quantity) {
    ProductStock productStock = productStockRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND));

    productStock.subtractProductStockByOrder(quantity);
  }

}
