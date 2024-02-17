package com.nayoon.ecommerce_service.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nayoon.product_service.common.exception.CustomException;
import com.nayoon.product_service.common.exception.ErrorCode;
import com.nayoon.product_service.product.controller.dto.request.ProductCreateRequestDto;
import com.nayoon.product_service.product.controller.dto.request.ProductUpdateRequestDto;
import com.nayoon.product_service.product.entity.Product;
import com.nayoon.product_service.product.entity.ProductStock;
import com.nayoon.product_service.product.repository.ProductRepository;
import com.nayoon.product_service.product.repository.ProductStockRepository;
import com.nayoon.product_service.product.service.ProductService;
import com.nayoon.product_service.product.service.dto.ProductDto;
import com.nayoon.product_service.product.service.dto.ProductStockDto;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
class ProductServiceTest {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private ProductStockRepository productStockRepository;

  @Nested
  @DisplayName("상품 등록")
  class create {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      ProductCreateRequestDto request = mockProductCreateRequest();
      Product product = mockProduct();

      when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {return product;});
      when(productStockRepository.save(any(ProductStock.class))).thenAnswer(invocation -> {
        return ProductStock.builder().productId(product.getId()).stock(request.stock()).build();
      });

      //when
      Long productId = productService.create(product.getId(), request.name(), request.content(), request.price(),
          request.stock());

      //then
      assertEquals(productId, product.getId());
      verify(productRepository, times(1)).save(any(Product.class));
      verify(productStockRepository, times(1)).save(any(ProductStock.class));
    }

  }

  @Nested
  @DisplayName("상품 수정")
  class update {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long principalId = 1L;
      ProductUpdateRequestDto request = mockProductUpdateRequest();
      Product product = mockProduct();
      productRepository.save(product);

      ProductStock productStock = mock(ProductStock.class);
      productStockRepository.save(productStock);

      when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
      when(productStockRepository.findById(product.getId())).thenReturn(Optional.of(productStock));

      //when
      productService.update(principalId, product.getId(), request.name(), request.content(), request.price(),
          request.stock());

      //then
      verify(productRepository, times(1)).save(any(Product.class));
      verify(productStockRepository, times(1)).save(any(ProductStock.class));
    }

  }

  @Nested
  @DisplayName("전체 상품 조회")
  class getAllProducts {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Pageable pageable = Pageable.ofSize(10).withPage(0);

      // Mock data for products
      List<Product> products = Arrays.asList(
          mockProduct(),
          mockProduct2()
      );
      when(productRepository.filterAllProducts(eq(pageable))).thenReturn(new PageImpl<>(products));

      //when
      Page<Product> result = productService.getAllProducts(pageable);

      //then
      assertEquals(products, result.getContent());
    }

  }

  @Nested
  @DisplayName("상품 재고 조회")
  class getProductStockById {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long productId = 1L;
      ProductStock productStock = new ProductStock(productId, 100);

      when(productStockRepository.findById(productId)).thenReturn(Optional.of(productStock));

      //when
      ProductStockDto response = productService.getProductStockById(productId);

      //then
      assertEquals(productId, response.productId());
      assertEquals(productStock.getStock(), response.stock());
    }

    @Test
    @DisplayName("실패: 재고 정보 찾을 수 없음")
    void productStockNotFound() {
      //given
      Long productId = 1L;

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> productService.getProductStockById(productId));

      //then
      assertEquals(ErrorCode.PRODUCT_STOCK_NOT_FOUND, exception.getErrorCode());
    }

  }

  @Nested
  @DisplayName("상품 상세정보 조회")
  class getProductInfoById {

    @Test
    @DisplayName("성공")
    void success() {
      //given
      Long productId = 1L;
      Product product = mockProduct();

      when(productRepository.findById(productId)).thenReturn(Optional.of(product));

      //when
      ProductDto response = productService.getProductInfoById(productId);

      //then
      assertEquals(product.getName(), response.name());
      assertEquals(product.getContent(), response.content());
      assertEquals(product.getPrice(), response.price());
    }

    @Test
    @DisplayName("실패: 상품 정보 찾을 수 없음")
    void productNotFound() {
      //given
      Long productId = 1L;

      //when
      CustomException exception = assertThrows(CustomException.class, ()
          -> productService.getProductInfoById(productId));

      //then
      assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

  }

  private Product mockProduct2() {
    return Product.builder()
        .userId(1L)
        .name("테스트 상품2")
        .content("테스트 상품2 내용")
        .price(20000L)
        .build();
  }

  private Product mockProduct() {
    return Product.builder()
        .userId(1L)
        .name("테스트 상품1")
        .content("테스트 상품1 내용")
        .price(10000L)
        .build();
  }

  private ProductCreateRequestDto mockProductCreateRequest() {
    return ProductCreateRequestDto.builder()
        .name("테스트 상품1")
        .content("테스트 상품1 내용")
        .price(10000L)
        .stock(100)
        .build();
  }

  private ProductUpdateRequestDto mockProductUpdateRequest() {
    return ProductUpdateRequestDto.builder()
        .content("테스트 상품1 새 내용")
        .build();
  }

}