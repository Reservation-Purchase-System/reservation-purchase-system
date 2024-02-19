package com.nayoon.purchase_service.controller;

import com.nayoon.purchase_service.controller.dto.request.PurchaseCreateRequestDto;
import com.nayoon.purchase_service.controller.dto.response.PurchaseResponseDto;
import com.nayoon.purchase_service.mapper.PurchaseResponseMapper;
import com.nayoon.purchase_service.service.PurchaseService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchases")
public class PurchaseController {

  private final PurchaseService purchaseService;

  /**
   * 주문 생성 - 결제 진입하면 주문 생성 요청
   */
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestHeader("X-USER-ID") String userId,
      @Valid @RequestBody PurchaseCreateRequestDto request
  ) {
    Long principalId = Long.valueOf(userId);
    String purchaseStatus = "pending"; // 결제 완료 전

    Long purchaseId = purchaseService.create(principalId, request.productId(), request.quantity(),
        request.address(), purchaseStatus);

    return ResponseEntity.created(URI.create("api/v1/purchases/" + purchaseId)).build();
  }

  /**
   * 주문 조회
   * - 결제까지 완료한 주문만 전달
   */
  @GetMapping
  public ResponseEntity<Page<PurchaseResponseDto>> getPurchasesByUserId(
      @RequestHeader("X-USER-ID") String userId,
      @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
      @RequestParam(value = "sortBy", defaultValue = "desc") String sortBy,
      @RequestParam(value = "pageCount", defaultValue = "20") int size,
      @RequestParam(value = "page", defaultValue = "0") int page
  ) {
    Sort.Direction direction = sortBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, orderBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Long principalId = Long.valueOf(userId);
    Page<PurchaseResponseDto> response = purchaseService.getPurchasesByUserId(principalId, pageable)
        .map(PurchaseResponseMapper::toDto);

    return ResponseEntity.ok().body(response);
  }

  /**
   * 주문 취소
   * - 결제 하기 전에 주문 취소
   */
  @DeleteMapping
  public ResponseEntity<Void> cancel(
      @RequestParam(name = "id") Long purchaseId
  ) {
    purchaseService.cancel(purchaseId);
    return ResponseEntity.ok().build();
  }

}
