package com.nayoon.purchase_service.entity;

import com.nayoon.purchase_service.type.ProductType;
import com.nayoon.purchase_service.type.PurchaseStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "purchase")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Purchase {

  @Id
  @Column(name = "purchase_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Enumerated(EnumType.STRING)
  @Column(name = "product_type")
  private ProductType productType;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "status", nullable = false)
  private PurchaseStatus purchaseStatus;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "deleted_at", nullable = false)
  private LocalDateTime deletedAt;

  @Builder
  public Purchase(Long userId, Long productId, Integer quantity, ProductType productType,
      String address, PurchaseStatus purchaseStatus) {
    this.userId = userId;
    this.productId = productId;
    this.quantity = quantity;
    this.productType = productType;
    this.address = address;
    this.purchaseStatus = purchaseStatus;
  }

  public void updateStatus(PurchaseStatus purchaseStatus) {
    this.purchaseStatus = purchaseStatus;
  }

  public void cancel() {
    this.deletedAt = LocalDateTime.now();
  }

}
