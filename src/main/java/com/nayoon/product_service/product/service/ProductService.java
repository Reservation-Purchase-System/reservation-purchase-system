package com.nayoon.product_service.product.service;

import com.nayoon.product_service.client.StockClient;
import com.nayoon.product_service.common.exception.CustomException;
import com.nayoon.product_service.common.exception.ErrorCode;
import com.nayoon.product_service.product.entity.Product;
import com.nayoon.product_service.product.repository.ProductRepository;
import com.nayoon.product_service.product.service.dto.ProductDto;
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
public class ProductService {

  private final ProductRepository productRepository;
  private final StockClient stockClient;

  /**
   * 상품 등록
   */
  @Transactional
  public Long create(Long principalId, String name, String content, Long price, Integer stock,
      Boolean isReserved, LocalDateTime openAt) {
    Product product = Product.builder()
        .userId(principalId)
        .name(name)
        .content(content)
        .price(price)
        .isReserved(isReserved)
        .openAt(openAt)
        .build();

    Product saved = productRepository.save(product);
    stockClient.createOrUpdate(saved.getId(), stock);

    return saved.getId();
  }

  /**
   * 상품 수정
   */
  @Transactional
  public void update(Long principalId, Long productId, String name, String content, Long price,
      Integer stock, Boolean isReserved, LocalDateTime openAt) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    checkProductOwner(principalId, product);

    stockClient.createOrUpdate(productId, stock);

    product.update(name, content, price, isReserved, openAt);
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

}
